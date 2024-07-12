package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public sealed abstract class NBT<T> permits NBTByteArray, NBTCompound, NBTEnd, NBTIntArray, NBTLongArray, NBTList, NBTPrimitive, NBTString {
    abstract void write(DataOutput output) throws IOException;

    public abstract String toString();

    public abstract Tag getTag();

    public abstract NBT<T> copy();

    public abstract T get();

    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof NBT<?> tag && getTag() == tag.getTag();
    }

    @Override
    public int hashCode() {
        return this.getTag().ordinal();
    }

    protected String getString() {
        return this.toString();
    }

    interface Reader<R> {
        NBT<R> read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException;
    }

    public static boolean deepEquals(NBT first, NBT second, boolean compareTagList) {
        if (first == second) {
            return true;
        } else if (first == null) {
            return true;
        } else if (second == null) {
            return false;
        } else if (first.getClass().equals(second.getClass())) {
            if (first instanceof NBTCompound fc) {
                return fc.deepEquals((NBTCompound) second, compareTagList);
            } else if (first instanceof NBTList fc && compareTagList) {
                return fc.deepEquals((NBTList) second);
            } else {
                return first.equals(second);
            }
        } else {
            return false;
        }
    }
}