package net.minecraft.nbt;

public abstract class CompoundWrapper {
    protected NBTCompound tag;

    public NBTCompound serialize() {
        return tag;
    }
}
