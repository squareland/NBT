package net.minecraft.nbt;

public abstract sealed class NBTPrimitive extends NBT permits NBTByte, NBTDouble, NBTFloat, NBTInt, NBTLong, NBTShort {
    public abstract long getLong();

    public abstract int getInt();

    public abstract short getShort();

    public abstract byte getByte();

    public abstract double getDouble();

    public abstract float getFloat();
}