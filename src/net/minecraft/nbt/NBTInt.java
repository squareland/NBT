package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class NBTInt extends NBTPrimitive {
    private final int data;

    public NBTInt(int data) {
        this.data = data;
    }

    public NBTInt(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(96L);
        this.data = input.readInt();
    }

    @Override
    void write(DataOutput output) throws IOException {
        output.writeInt(this.data);
    }

    @Override
    public Tag getTag() {
        return Tag.INT;
    }

    @Override
    public String toString() {
        return String.valueOf(data);
    }

    @Override
    public NBTInt copy() {
        return this;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof NBTInt i && data == i.data;
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
        return (short) (data & 65535);
    }

    @Override
    public byte getByte() {
        return (byte) (data & 255);
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