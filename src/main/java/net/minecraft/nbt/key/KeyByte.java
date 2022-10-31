package net.minecraft.nbt.key;

import net.minecraft.nbt.NBTByte;
import net.minecraft.nbt.Tag;

public class KeyByte extends KeyPrimitive<NBTByte> {
    KeyByte(String name) {
        super(name, NBTByte.class, Tag.BYTE);
    }
}
