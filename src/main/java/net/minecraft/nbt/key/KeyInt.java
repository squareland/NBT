package net.minecraft.nbt.key;

import net.minecraft.nbt.NBTInt;
import net.minecraft.nbt.Tag;

public class KeyInt extends KeyPrimitive<NBTInt> {
    KeyInt(String name) {
        super(name, NBTInt.class, Tag.INT);
    }
}
