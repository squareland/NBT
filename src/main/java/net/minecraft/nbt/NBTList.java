package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public final class NBTList extends NBT<List<NBT<?>>> implements Iterable<NBT<?>> {
    private final List<NBT<?>> tags;
    private Tag tag;

    NBTList(List<NBT<?>> tags, Tag tag) {
        this.tags = tags;
        this.tag = tag;
    }

    public NBTList() {
        this(new ArrayList<>(), Tag.END);
    }

    public NBTList(List<? extends CompoundWrapper> source) {
        this.tag = Tag.COMPOUND;
        this.tags = new ArrayList<>(source.size());
        for (int i = 0; i < source.size(); i++) {
            this.tags.add(source.get(i).serialize());
        }
    }

    public NBTList(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(296L);

        if (depth > 512) {
            throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
        } else {
            this.tag = Tag.values()[input.readByte()];
            int i = input.readInt();

            if (this.tag == Tag.END && i > 0) {
                throw new RuntimeException("Missing type on ListTag");
            } else {
                sizeTracker.read(32L * (long) i);
                this.tags = new ArrayList<>(i);

                for (int j = 0; j < i; ++j) {
                    NBT<?> nbt = this.tag.read(input, depth + 1, sizeTracker);
                    this.tags.add(nbt);
                }
            }
        }
    }

    @Override
    void write(DataOutput output) throws IOException {
        if (this.tags.isEmpty()) {
            this.tag = Tag.END;
        } else {
            this.tag = this.tags.get(0).getTag();
        }

        output.writeByte(this.tag.ordinal());
        output.writeInt(this.tags.size());

        for (NBT<?> nbt : this.tags) {
            nbt.write(output);
        }
    }

    @Override
    public Tag getTag() {
        return Tag.LIST;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");

        for (int i = 0; i < this.tags.size(); ++i) {
            if (i != 0) {
                result.append(',');
            }

            result.append(this.tags.get(i));
        }

        return result.append(']').toString();
    }

    private void validate(Tag type) {
        if (tag == Tag.END) {
            tag = type;
        } else if (tag != type) {
            throw new TagMismatchException("list[" + size() + "]", type, tag);
        }
    }

    public void push(byte value) {
        validate(Tag.BYTE);
        tags.add(new NBTByte(value));
    }

    public void push(short value) {
        validate(Tag.SHORT);
        tags.add(new NBTShort(value));
    }

    public void push(int value) {
        validate(Tag.INT);
        tags.add(new NBTInt(value));
    }

    public void push(long value) {
        validate(Tag.LONG);
        tags.add(new NBTLong(value));
    }

    public void push(float value) {
        validate(Tag.FLOAT);
        tags.add(new NBTFloat(value));
    }

    public void push(double value) {
        validate(Tag.DOUBLE);
        tags.add(new NBTDouble(value));
    }

    public void push(String value) {
        validate(Tag.STRING);
        tags.add(new NBTString(value));
    }

    public void push(byte[] value) {
        validate(Tag.BYTE_ARRAY);
        tags.add(new NBTByteArray(value));
    }

    public void push(int[] value) {
        validate(Tag.INT_ARRAY);
        tags.add(new NBTIntArray(value));
    }

    public void push(long[] value) {
        validate(Tag.LONG_ARRAY);
        tags.add(new NBTLongArray(value));
    }

    public void push(NBTCompound value) {
        validate(Tag.COMPOUND);
        tags.add(value);
    }

    public void push(NBTList value) {
        validate(Tag.LIST);
        tags.add(value);
    }

    public void push(NBT<?> tag) {
        switch (tag) {
            case NBTEnd end -> {}
            case NBTByteArray array -> push(array.get());
            case NBTIntArray array -> push(array.get());
            case NBTLongArray array -> push(array.get());
            case NBTCompound compound -> push(compound);
            case NBTList list -> push(list);
            case NBTLong number -> push(number.getLong());
            case NBTInt number -> push(number.getInt());
            case NBTString string -> push(string.getString());
            case NBTByte number -> push(number.getByte());
            case NBTShort number -> push(number.getShort());
            case NBTDouble number -> push(number.getDouble());
            case NBTFloat number -> push(number.getFloat());
        }
    }

    public void set(int index, byte[] value) {
        validate(Tag.BYTE_ARRAY);
        tags.set(index, new NBTByteArray(value));
    }

    public void set(int index, int[] value) {
        validate(Tag.INT_ARRAY);
        tags.set(index, new NBTIntArray(value));
    }

    public void set(int index, long[] value) {
        validate(Tag.LONG_ARRAY);
        tags.set(index, new NBTLongArray(value));
    }

    public void set(int index, NBTCompound value) {
        validate(Tag.COMPOUND);
        tags.set(index, value);
    }

    public void set(int index, NBTList value) {
        validate(Tag.LIST);
        tags.set(index, value);
    }

    public void set(int index, long value) {
        validate(Tag.LONG);
        tags.set(index, new NBTLong(value));
    }

    public void set(int index, int value) {
        validate(Tag.INT);
        tags.set(index, new NBTInt(value));
    }

    public void set(int index, String value) {
        validate(Tag.STRING);
        tags.set(index, new NBTString(value));
    }

    public void set(int index, byte value) {
        validate(Tag.BYTE);
        tags.set(index, new NBTByte(value));
    }

    public void set(int index, short value) {
        validate(Tag.SHORT);
        tags.set(index, new NBTShort(value));
    }

    public void set(int index, double value) {
        validate(Tag.DOUBLE);
        tags.set(index, new NBTDouble(value));
    }

    public void set(int index, float value) {
        validate(Tag.FLOAT);
        tags.set(index, new NBTFloat(value));
    }

    public NBT remove(int index) {
        return this.tags.remove(index);
    }

    @Override
    public boolean isEmpty() {
        return this.tags.isEmpty();
    }

    public void clear() {
        this.tags.clear();
    }

    public NBTCompound getCompound(int index) {
        NBT<?> tag = tags.get(index);
        if (tag instanceof NBTCompound compound) {
            return compound;
        }
        if (tag != null) {
            throw new TagMismatchException("list[" + index + "]", Tag.COMPOUND, tag.getTag());
        }
        return new NBTCompound();
    }

    public int getInt(int index) {
        NBT<?> tag = tags.get(index);
        if (tag instanceof NBTPrimitive<?> primitive) {
            return primitive.getInt();
        }
        if (tag != null) {
            throw new TagMismatchException("list[" + index + "]", Tag.INT, tag.getTag());
        }
        return 0;
    }

    public int[] getIntArray(int index) {
        NBT<?> tag = tags.get(index);
        if (tag instanceof NBTIntArray array) {
            return array.get();
        }
        if (tag != null) {
            throw new TagMismatchException("list[" + index + "]", Tag.INT_ARRAY, tag.getTag());
        }
        return new int[0];
    }

    public long[] getLongArray(int index) {
        NBT<?> tag = tags.get(index);
        if (tag instanceof NBTLongArray array) {
            return array.get();
        }
        if (tag != null) {
            throw new TagMismatchException("list[" + index + "]", Tag.LONG_ARRAY, tag.getTag());
        }
        return new long[0];
    }

    public double getDouble(int index) {
        NBT<?> tag = tags.get(index);
        if (tag instanceof NBTPrimitive<?> primitive) {
            return primitive.getDouble();
        }
        if (tag != null) {
            throw new TagMismatchException("list[" + index + "]", Tag.DOUBLE, tag.getTag());
        }
        return 0;
    }

    public float getFloat(int index) {
        NBT<?> tag = tags.get(index);
        if (tag instanceof NBTPrimitive<?> primitive) {
            return primitive.getFloat();
        }
        if (tag != null) {
            throw new TagMismatchException("list[" + index + "]", Tag.FLOAT, tag.getTag());
        }
        return 0;
    }

    public String getString(int index) {
        NBT<?> tag = tags.get(index);
        if (tag instanceof NBTString string) {
            return string.getString();
        }
        if (tag != null) {
            throw new TagMismatchException("list[" + index + "]", Tag.STRING, tag.getTag());
        }
        return "";
    }

    public NBT<?> get(int index) {
        return tags.get(index);
    }

    public int size() {
        return tags.size();
    }

    @Override
    public NBTList copy() {
        List<NBT<?>> copy = new ArrayList<>(tags.size());
        for (int i = 0; i < tags.size(); i++) {
            copy.add(tags.get(i).copy());
        }
        return new NBTList(copy, tag);
    }

    @Override
    public List<NBT<?>> get() {
        return tags;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof NBTList list && tag == list.tag && Objects.equals(tags, list.tags);
    }

    public boolean deepEquals(NBTList other) {
        if (isEmpty()) {
            return other.isEmpty();
        } else {
            for (int i = 0; i < size(); ++i) {
                NBT<?> a = get(i);
                boolean found = false;

                for (int j = 0; j < other.size(); ++j) {
                    NBT<?> b = other.get(j);
                    if (deepEquals(a, b, true)) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    return false;
                }
            }

            return true;
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ tags.hashCode();
    }

    public Tag getTagType() {
        return tag;
    }

    @Override
    public Iterator<NBT<?>> iterator() {
        return tags.iterator();
    }
}