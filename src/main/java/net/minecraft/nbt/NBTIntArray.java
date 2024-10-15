package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;

public final class NBTIntArray extends NBT<int[]> {
    private static final VarHandle INT = MethodHandles.byteArrayViewVarHandle(int[].class, ByteOrder.BIG_ENDIAN);

    private final int[] array;

    public NBTIntArray(int[] array) {
        this.array = array;
    }

    public NBTIntArray(List<Integer> list) {
        this(toArray(list));
    }

    public NBTIntArray(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(192L);
        int i = input.readInt();
        sizeTracker.read(32 * i);
        byte[] bytes = new byte[4 * i];
        input.readFully(bytes);
        this.array = new int[i];
        for (int j = 0; j < i; j++) {
            this.array[j] = (int) INT.get(bytes, 4 * j);
        }
    }

    private static int[] toArray(List<Integer> list) {
        int[] array = new int[list.size()];

        for (int i = 0; i < list.size(); ++i) {
            Integer integer = list.get(i);
            array[i] = integer == null ? 0 : integer;
        }

        return array;
    }

    @Override
    void write(DataOutput output) throws IOException {
        output.writeInt(this.array.length);

        for (int i : this.array) {
            output.writeInt(i);
        }
    }

    @Override
    public Tag getTag() {
        return Tag.INT_ARRAY;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[I;");

        for (int i = 0; i < this.array.length; ++i) {
            if (i != 0) {
                result.append(',');
            }

            result.append(this.array[i]);
        }

        return result.append(']').toString();
    }

    @Override
    public NBTIntArray copy() {
        int[] array = new int[this.array.length];
        System.arraycopy(this.array, 0, array, 0, this.array.length);
        return new NBTIntArray(array);
    }

    @Override
    public int[] get() {
        return array;
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other) && Arrays.equals(this.array, ((NBTIntArray) other).array);
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ Arrays.hashCode(this.array);
    }
}