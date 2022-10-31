package net.minecraft.nbt.key;

import net.minecraft.nbt.NBTShort;
import net.minecraft.nbt.Tag;

public class KeyShort extends KeyPrimitive<NBTShort> {
    KeyShort(String name) {
        super(name, NBTShort.class, Tag.SHORT);
    }
}
