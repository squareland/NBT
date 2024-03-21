package net.minecraft.nbt;

public abstract class CompoundWrapper {
    protected final NBTCompound tag;

    protected CompoundWrapper(NBTCompound tag) {
        this.tag = tag;
    }
}
