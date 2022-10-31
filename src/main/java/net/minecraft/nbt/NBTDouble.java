package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class NBTDouble extends NBTPrimitive<Double> {
    private final double data;

    public NBTDouble(double data) {
        this.data = data;
    }

    public NBTDouble(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(128L);
        this.data = input.readDouble();
    }

    @Override
    void write(DataOutput output) throws IOException {
        output.writeDouble(data);
    }

    @Override
    public Tag getTag() {
        return Tag.DOUBLE;
    }

    public String toString() {
        return data + "d";
    }

    @Override
    public NBTDouble copy() {
        return this;
    }

    @Override
    public Double get() {
        return data;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof NBTDouble d && data == d.data;
    }

    @Override
    public int hashCode() {
        long i = Double.doubleToLongBits(data);
        return super.hashCode() ^ (int) (i ^ i >>> 32);
    }

    @Override
    public long getLong() {
        return (long) Math.floor(data);
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
        return data;
    }

    @Override
    public float getFloat() {
        return (float) data;
    }
}