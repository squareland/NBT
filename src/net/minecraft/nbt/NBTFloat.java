package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class NBTFloat extends NBTPrimitive {
    private final float data;

    public NBTFloat(float data) {
        this.data = data;
    }

    public NBTFloat(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(96L);
        this.data = input.readFloat();
    }

    @Override
    void write(DataOutput output) throws IOException {
        output.writeFloat(this.data);
    }

    @Override
    public Tag getTag() {
        return Tag.FLOAT;
    }

    public String toString() {
        return data + "f";
    }

    @Override
    public NBTFloat copy() {
        return this;
    }

    public boolean equals(Object other) {
        return other instanceof NBTFloat f && f.data == f.data;
    }

    public int hashCode() {
        return super.hashCode() ^ Float.floatToIntBits(data);
    }

    @Override
    public long getLong() {
        return (long) data;
    }

    @Override
    public int getInt() {
        return (int) Math.floor(data);
    }

    @Override
    public short getShort() {
        return (short) (getInt() & 65535);
    }

    @Override
    public byte getByte() {
        return (byte) (getInt() & 255);
    }

    @Override
    public double getDouble() {
        return this.data;
    }

    @Override
    public float getFloat() {
        return this.data;
    }
}