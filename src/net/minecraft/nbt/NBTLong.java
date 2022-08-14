package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class NBTLong extends NBTPrimitive {
    private final long data;

    public NBTLong(long data) {
        this.data = data;
    }

    public NBTLong(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(128L);
        this.data = input.readLong();
    }

    @Override
    void write(DataOutput output) throws IOException {
        output.writeLong(this.data);
    }

    @Override
    public Tag getTag() {
        return Tag.LONG;
    }

    public String toString() {
        return this.data + "L";
    }

    @Override
    public NBTLong copy() {
        return this;
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other) && this.data == ((NBTLong) other).data;
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ (int) (this.data ^ this.data >>> 32);
    }

    @Override
    public long getLong() {
        return this.data;
    }

    @Override
    public int getInt() {
        return (int) this.data;
    }

    @Override
    public short getShort() {
        return (short) (int) (this.data & 65535L);
    }

    @Override
    public byte getByte() {
        return (byte) (int) (this.data & 255L);
    }

    @Override
    public double getDouble() {
        return (double) this.data;
    }

    @Override
    public float getFloat() {
        return (float) this.data;
    }
}