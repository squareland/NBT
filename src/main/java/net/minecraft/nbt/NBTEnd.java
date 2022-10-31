package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;

public final class NBTEnd extends NBT<Void> {
    public NBTEnd() {
    }

    public NBTEnd(DataInput input, int depth, NBTSizeTracker sizeTracker) {
        sizeTracker.read(64L);
    }

    @Override
    void write(DataOutput output) {
    }

    @Override
    public Tag getTag() {
        return Tag.END;
    }

    @Override
    public String toString() {
        return "END";
    }

    @Override
    public NBTEnd copy() {
        return this;
    }

    @Override
    public Void get() {
        throw new UnsupportedOperationException();
    }
}