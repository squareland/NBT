package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class NBTByte extends NBTPrimitive<Byte> {
    private final byte data;

    public NBTByte(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(72L);
        this.data = input.readByte();
    }

    public NBTByte(byte data) {
        this.data = data;
    }

    @Override
    void write(DataOutput output) throws IOException {
        output.writeByte(data);
    }

    @Override
    public Tag getTag() {
        return Tag.BYTE;
    }

    public String toString() {
        return data + "b";
    }

    @Override
    public NBTByte copy() {
        return this;
    }

    @Override
    public Byte get() {
        return data;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof NBTByte b && data == b.data;
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ data;
    }

    @Override
    public long getLong() {
        return data;
    }

    @Override
    public int getInt() {
        return data;
    }

    @Override
    public short getShort() {
        return data;
    }

    @Override
    public byte getByte() {
        return data;
    }

    @Override
    public double getDouble() {
        return data;
    }

    @Override
    public float getFloat() {
        return data;
    }
}