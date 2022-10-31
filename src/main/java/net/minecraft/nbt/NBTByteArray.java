package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

public final class NBTByteArray extends NBT<byte[]> {
    private final byte[] data;

    public NBTByteArray(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(192L);
        int i = input.readInt();
        sizeTracker.read(8L * i);
        this.data = new byte[i];
        input.readFully(this.data);
    }

    public NBTByteArray(byte[] data) {
        this.data = data;
    }

    public NBTByteArray(List<Byte> bytes) {
        this(toArray(bytes));
    }

    public NBTByteArray(ByteBuffer bytes) {
        this(toArray(bytes));
    }

    private static byte[] toArray(List<Byte> bytes) {
        byte[] output = new byte[bytes.size()];

        for (int i = 0; i < bytes.size(); ++i) {
            Byte b = bytes.get(i);
            output[i] = b == null ? 0 : b;
        }

        return output;
    }

    private static byte[] toArray(ByteBuffer bytes) {
        byte[] output = new byte[bytes.capacity()];

        for (int i = 0; i < bytes.capacity(); ++i) {
            output[i] = bytes.get(i);
        }

        bytes.rewind();

        return output;
    }

    @Override
    void write(DataOutput output) throws IOException {
        output.writeInt(data.length);
        output.write(data);
    }

    @Override
    public Tag getTag() {
        return Tag.BYTE_ARRAY;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[B;");

        for (int i = 0; i < data.length; ++i) {
            if (i != 0) {
                result.append(',');
            }

            result.append(data[i]).append('B');
        }

        return result.append(']').toString();
    }

    @Override
    public NBTByteArray copy() {
        byte[] bytes = new byte[data.length];
        System.arraycopy(data, 0, bytes, 0, data.length);
        return new NBTByteArray(bytes);
    }

    @Override
    public byte[] get() {
        return data;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof NBTByteArray a && Arrays.equals(data, a.data);
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ Arrays.hashCode(data);
    }

    public byte[] getByteArray() {
        return data;
    }
}