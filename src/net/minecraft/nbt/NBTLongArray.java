package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public final class NBTLongArray extends NBT {
    private final long[] array;

    public NBTLongArray(long[] array) {
        this.array = array;
    }

    public NBTLongArray(List<Long> list) {
        this(toArray(list));
    }

    public NBTLongArray(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(192L);
        int i = input.readInt();
        sizeTracker.read(64 * i);
        this.array = new long[i];

        for (int j = 0; j < i; ++j) {
            this.array[j] = input.readLong();
        }
    }

    private static long[] toArray(List<Long> list) {
        long[] array = new long[list.size()];

        for (int i = 0; i < list.size(); ++i) {
            Long l = list.get(i);
            array[i] = l == null ? 0L : l;
        }

        return array;
    }

    @Override
    void write(DataOutput output) throws IOException {
        output.writeInt(this.array.length);

        for (long i : this.array) {
            output.writeLong(i);
        }
    }

    @Override
    public Tag getTag() {
        return Tag.LONG_ARRAY;
    }

    public String toString() {
        StringBuilder result = new StringBuilder("[L;");

        for (int i = 0; i < this.array.length; ++i) {
            if (i != 0) {
                result.append(',');
            }

            result.append(this.array[i]).append('L');
        }

        return result.append(']').toString();
    }

    @Override
    public NBTLongArray copy() {
        long[] array = new long[this.array.length];
        System.arraycopy(this.array, 0, array, 0, this.array.length);
        return new NBTLongArray(array);
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other) && Arrays.equals(this.array, ((NBTLongArray) other).array);
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ Arrays.hashCode(this.array);
    }
}