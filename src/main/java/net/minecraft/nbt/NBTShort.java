package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class NBTShort extends NBTPrimitive<Short> implements Comparable<NBTShort> {
    private final short data;

    public NBTShort(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(80L);
        this.data = input.readShort();
    }

    public NBTShort(short data) {
        this.data = data;
    }

    @Override
    void write(DataOutput output) throws IOException {
        output.writeShort(this.data);
    }

    @Override
    public Tag getTag() {
        return Tag.SHORT;
    }

    public String toString() {
        return this.data + "s";
    }

    @Override
    public NBTShort copy() {
        return this;
    }

    @Override
    public Short get() {
        return data;
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other) && this.data == ((NBTShort) other).data;
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ this.data;
    }

    @Override
    public long getLong() {
        return this.data;
    }

    @Override
    public int getInt() {
        return this.data;
    }

    @Override
    public short getShort() {
        return this.data;
    }

    @Override
    public byte getByte() {
        return (byte) (this.data & 255);
    }

    @Override
    public double getDouble() {
        return this.data;
    }

    @Override
    public float getFloat() {
        return this.data;
    }

    @Override
    public int compareTo(NBTShort o) {
        return Short.compare(data, o.data);
    }
}