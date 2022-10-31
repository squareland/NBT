package net.minecraft.nbt.key;

import net.minecraft.nbt.NBTCompound;
import net.minecraft.nbt.Tag;

public class KeyCompound extends Key<NBTCompound> {
    KeyCompound(String name) {
        super(name, NBTCompound.class, Tag.COMPOUND);
    }
}
