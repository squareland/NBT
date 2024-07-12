package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

public final class NBTCompound extends NBT<Map<String, NBT<?>>> implements CompoundRead, CompoundWrite {
    private static final Pattern SIMPLE_VALUE = Pattern.compile("[A-Za-z0-9._+-]+");

    private final Map<String, NBT<?>> tags;

    NBTCompound(Map<String, NBT<?>> tags) {
        this.tags = tags;
    }

    public NBTCompound() {
        this(new HashMap<>());
    }

    public NBTCompound(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        this(new HashMap<>());
        sizeTracker.read(384L);

        if (depth > 512) {
            throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
        } else {
            Tag id;

            while ((id = readType(input, sizeTracker)) != Tag.END) {
                String s = readKey(input, sizeTracker);
                sizeTracker.read(224 + 16L * s.length());
                NBT<?> nbt = readNBT(id, s, input, depth + 1, sizeTracker);

                if (this.tags.put(s, nbt) != null) {
                    sizeTracker.read(288L);
                }
            }
        }
    }

    private static void writeEntry(String name, NBT<?> data, DataOutput output) throws IOException {
        output.writeByte(data.getTag().ordinal());

        if (data.getTag() != Tag.END) {
            output.writeUTF(name);
            data.write(output);
        }
    }

    private static Tag readType(DataInput input, NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(8);
        return Tag.values()[input.readByte()];
    }

    private static String readKey(DataInput input, NBTSizeTracker sizeTracker) throws IOException {
        return input.readUTF();
    }

    static NBT<?> readNBT(Tag tag, String key, DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(32); //Forge: 4 extra bytes for the object allocation.
        return tag.read(input, depth, sizeTracker);
    }

    private static String handleEscape(String string) {
        return SIMPLE_VALUE.matcher(string).matches() ? string : NBTString.quoteAndEscape(string);
    }

    @Override
    void write(DataOutput output) throws IOException {
        for (Map.Entry<String, NBT<?>> e : tags.entrySet()) {
            writeEntry(e.getKey(), e.getValue(), output);
        }

        output.writeByte(0);
    }

    public Set<String> keySet() {
        return this.tags.keySet();
    }

    public Set<Map.Entry<String, NBT<?>>> entrySet() {
        return tags.entrySet();
    }

    @Override
    public Tag getTag() {
        return Tag.COMPOUND;
    }

    public int size() {
        return tags.size();
    }

    @Override
    public void set(String key, NBT value) {
        tags.put(key, value);
    }

    @Override
    public NBT<?> get(String key) {
        return tags.get(key);
    }

    @Override
    public NBT<?> replace(String key, NBT<?> value) {
        return tags.replace(key, value);
    }

    @Override
    public NBT<?> merge(String key, NBT<?> value, BiFunction<? super NBT<?>, ? super NBT<?>, ? extends NBT<?>> mapping) {
        return tags.merge(key, value, mapping);
    }

    @Override
    public NBT<?> compute(String key, BiFunction<String, ? super NBT<?>, ? extends NBT<?>> mapping) {
        return tags.compute(key, mapping);
    }

    @Override
    public NBT<?> remove(String key) {
        return tags.remove(key);
    }

    @Override
    public Tag getTagId(String key) {
        NBT<?> tag = tags.get(key);
        return tag == null ? Tag.END : tag.getTag();
    }

    @Override
    public boolean hasKey(String key) {
        return tags.containsKey(key);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("{");
        Collection<String> keys = tags.keySet();

        for (String key : keys) {
            if (result.length() != 1) {
                result.append(',');
            }

            result.append(handleEscape(key)).append(':').append(tags.get(key));
        }

        return result.append('}').toString();
    }

    @Override
    public boolean isEmpty() {
        return tags.isEmpty();
    }

    @Override
    public NBTCompound copy() {
        NBTCompound copy = new NBTCompound(new HashMap<>(tags.size()));
        for (Map.Entry<String, NBT<?>> e : tags.entrySet()) {
            copy.set(e.getKey(), e.getValue().copy());
        }
        return copy;
    }

    @Override
    public Map<String, NBT<?>> get() {
        return tags;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof NBTCompound c && Objects.equals(tags.entrySet(), c.tags.entrySet());
    }

    public boolean deepEquals(NBTCompound other, boolean compareTagList) {
        for (Map.Entry<String, NBT<?>> e : entrySet()) {
            String key = e.getKey();
            NBT<?> a = e.getValue();
            NBT<?> b = other.get(key);

            if (!deepEquals(a, b, compareTagList)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ tags.hashCode();
    }

    public void merge(NBTCompound other) {
        for (Map.Entry<String, NBT<?>> e : other.entrySet()) {
            String key = e.getKey();
            NBT<?> value = e.getValue();
            if (value instanceof NBTCompound compound) {
                if (tags.get(key) instanceof NBTCompound pair) {
                    pair.merge(compound);
                } else {
                    set(key, value.copy());
                }
            } else {
                set(key, value.copy());
            }
        }
    }
}