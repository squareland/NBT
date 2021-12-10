package net.minecraft.nbt;

import net.minecraft.nbt.factory.ByteSupplier;
import net.minecraft.nbt.factory.FloatSupplier;
import net.minecraft.nbt.factory.ShortSupplier;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.function.*;
import java.util.regex.Pattern;

public final class NBTCompound extends NBT {
    private static final Pattern SIMPLE_VALUE = Pattern.compile("[A-Za-z0-9._+-]+");

    private final Map<String, NBT> tags = new HashMap<>();

    public NBTCompound() {
    }

    public NBTCompound(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(384L);

        if (depth > 512) {
            throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
        } else {
            Tag id;

            while ((id = readType(input, sizeTracker)) != Tag.END) {
                String s = readKey(input, sizeTracker);
                sizeTracker.read(224 + 16L * s.length());
                NBT nbt = readNBT(id, s, input, depth + 1, sizeTracker);

                if (this.tags.put(s, nbt) != null) {
                    sizeTracker.read(288L);
                }
            }
        }
    }

    private static void writeEntry(String name, NBT data, DataOutput output) throws IOException {
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

    static NBT readNBT(Tag tag, String key, DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(32); //Forge: 4 extra bytes for the object allocation.
        return tag.read(input, depth, sizeTracker);
    }

    private static String handleEscape(String string) {
        return SIMPLE_VALUE.matcher(string).matches() ? string : NBTString.quoteAndEscape(string);
    }

    @Override
    void write(DataOutput output) throws IOException {
        for (Map.Entry<String, NBT> e : tags.entrySet()) {
            writeEntry(e.getKey(), e.getValue(), output);
        }

        output.writeByte(0);
    }

    public Set<String> keySet() {
        return this.tags.keySet();
    }

    public Set<Map.Entry<String, NBT>> entrySet() {
        return tags.entrySet();
    }

    @Override
    public Tag getTag() {
        return Tag.COMPOUND;
    }

    public int size() {
        return this.tags.size();
    }

    public void setTag(String key, NBT value) {
        if (value == null) {
            throw new IllegalArgumentException("Invalid null NBT value with key " + key);
        }
        this.tags.put(key, value);
    }

    public void setByte(String key, byte value) {
        this.tags.put(key, new NBTByte(value));
    }

    public void setShort(String key, short value) {
        this.tags.put(key, new NBTShort(value));
    }

    public void setInteger(String key, int value) {
        this.tags.put(key, new NBTInt(value));
    }

    public void setLong(String key, long value) {
        this.tags.put(key, new NBTLong(value));
    }

    public void setUniqueId(String key, UUID value) {
        this.setLong(key + "Most", value.getMostSignificantBits());
        this.setLong(key + "Least", value.getLeastSignificantBits());
    }

    public UUID getUniqueId(String key) {
        return new UUID(this.getLong(key + "Most"), this.getLong(key + "Least"));
    }

    public boolean hasUniqueId(String key) {
        return this.hasNumericKey(key + "Most") && this.hasNumericKey(key + "Least");
    }

    public void setFloat(String key, float value) {
        this.tags.put(key, new NBTFloat(value));
    }

    public void setDouble(String key, double value) {
        this.tags.put(key, new NBTDouble(value));
    }

    public void setString(String key, String value) {
        this.tags.put(key, new NBTString(value));
    }

    public void setByteArray(String key, byte[] value) {
        this.tags.put(key, new NBTByteArray(value));
    }

    public void setByteArray(String key, List<Byte> value) {
        this.tags.put(key, new NBTByteArray(value));
    }

    public void setByteArray(String key, ByteBuffer value) {
        this.tags.put(key, new NBTByteArray(value));
    }

    public void setIntArray(String key, int[] value) {
        this.tags.put(key, new NBTIntArray(value));
    }

    public void setBoolean(String key, boolean value) {
        this.setByte(key, (byte) (value ? 1 : 0));
    }

    public NBT getTag(String key) {
        return tags.get(key);
    }

    public Tag getTagId(String key) {
        NBT tag = tags.get(key);
        return tag == null ? Tag.END : tag.getTag();
    }

    public boolean hasKey(String key) {
        return this.tags.containsKey(key);
    }

    public boolean hasKey(String key, Tag tag) {
        Tag type = getTagId(key);
        return type == tag;
    }

    public boolean hasNumericKey(String key) {
        Tag type = getTagId(key);
        return type == Tag.BYTE || type == Tag.SHORT || type == Tag.INT || type == Tag.LONG || type == Tag.FLOAT || type == Tag.DOUBLE;
    }

    public byte getByte(String key) {
        return getByte(key, () -> (byte) 0);
    }

    public byte getByte(String key, ByteSupplier fallback) {
        NBT tag = tags.get(key);
        if (tag instanceof NBTPrimitive primitive) {
            return primitive.getByte();
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.BYTE, tag.getTag());
        }
        return fallback.getAsByte();
    }

    public short getShort(String key) {
        return getShort(key, () -> (short) 0);
    }

    public short getShort(String key, ShortSupplier fallback) {
        NBT tag = tags.get(key);
        if (tag instanceof NBTPrimitive primitive) {
            return primitive.getShort();
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.SHORT, tag.getTag());
        }
        return fallback.getAsShort();
    }

    public int getInteger(String key) {
        return getInteger(key, () -> 0);
    }

    public int getInteger(String key, IntSupplier fallback) {
        NBT tag = tags.get(key);
        if (tag instanceof NBTPrimitive primitive) {
            return primitive.getInt();
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.INT, tag.getTag());
        }
        return fallback.getAsInt();
    }

    public long getLong(String key) {
        return getLong(key, () -> 0);
    }

    public long getLong(String key, LongSupplier fallback) {
        NBT tag = tags.get(key);
        if (tag instanceof NBTPrimitive primitive) {
            return primitive.getLong();
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.LONG, tag.getTag());
        }
        return fallback.getAsLong();
    }

    public float getFloat(String key) {
        return getFloat(key, () -> 0);
    }

    public float getFloat(String key, FloatSupplier fallback) {
        NBT tag = tags.get(key);
        if (tag instanceof NBTPrimitive primitive) {
            return primitive.getFloat();
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.FLOAT, tag.getTag());
        }
        return fallback.getAsFloat();
    }

    public double getDouble(String key) {
        return getDouble(key, () -> 0);
    }

    public double getDouble(String key, DoubleSupplier fallback) {
        NBT tag = tags.get(key);
        if (tag instanceof NBTPrimitive primitive) {
            return primitive.getDouble();
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.DOUBLE, tag.getTag());
        }
        return fallback.getAsDouble();
    }

    public String getString(String key) {
        return getString(key, String::new);
    }

    public String getString(String key, Supplier<String> fallback) {
        NBT tag = tags.get(key);
        if (tag instanceof NBTString string) {
            return string.getString();
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.STRING, tag.getTag());
        }
        return fallback.get();
    }

    public byte[] getByteArray(String key) {
        return getByteArray(key, () -> new byte[0]);
    }

    public byte[] getByteArray(String key, Supplier<byte[]> fallback) {
        NBT tag = tags.get(key);
        if (tag instanceof NBTByteArray array) {
            return array.getByteArray();
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.BYTE_ARRAY, tag.getTag());
        }
        return fallback.get();
    }

    public int[] getIntArray(String key) {
        return getIntArray(key, () -> new int[0]);
    }

    public int[] getIntArray(String key, Supplier<int[]> fallback) {
        NBT tag = tags.get(key);
        if (tag instanceof NBTIntArray array) {
            return array.getIntArray();
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.INT_ARRAY, tag.getTag());
        }
        return fallback.get();
    }

    public NBTCompound getCompound(String key) {
        return getCompound(key, NBTCompound::new);
    }

    public NBTCompound getCompound(String key, Supplier<NBTCompound> fallback) {
        NBT tag = tags.get(key);
        if (tag instanceof NBTCompound compound) {
            return compound;
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.COMPOUND, tag.getTag());
        }
        return fallback.get();
    }

    public NBTList getTagList(String key, Tag type) {
        return getTagList(key, type, NBTList::new);
    }

    public NBTList getTagList(String key, Tag type, Supplier<NBTList> fallback) {
        NBT tag = tags.get(key);
        if (tag instanceof NBTList list) {
            if (list.isEmpty() || list.getTagType() == type) {
                return list;
            }
        }
        return fallback.get();
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, () -> false);
    }

    public boolean getBoolean(String key, BooleanSupplier fallback) {
        NBT tag = tags.get(key);
        if (tag instanceof NBTPrimitive primitive) {
            return primitive.getByte() != 0;
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.BYTE, tag.getTag());
        }
        return fallback.getAsBoolean();
    }

    public NBT removeTag(String key) {
        return tags.remove(key);
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
        NBTCompound copy = new NBTCompound();
        for (Map.Entry<String, NBT> e : tags.entrySet()) {
            copy.setTag(e.getKey(), e.getValue().copy());
        }
        return copy;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof NBTCompound c && Objects.equals(tags.entrySet(), c.tags.entrySet());
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ tags.hashCode();
    }

    public void merge(NBTCompound other) {
        for (Map.Entry<String, NBT> e : other.entrySet()) {
            String key = e.getKey();
            NBT value = e.getValue();
            if (value instanceof NBTCompound compound) {
                if (tags.get(key) instanceof NBTCompound pair) {
                    pair.merge(compound);
                } else {
                    setTag(key, value.copy());
                }
            } else {
                setTag(key, value.copy());
            }
        }
    }
}