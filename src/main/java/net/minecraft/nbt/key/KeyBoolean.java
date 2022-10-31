package net.minecraft.nbt.key;

import net.minecraft.nbt.NBTByte;
import net.minecraft.nbt.Tag;

public class KeyBoolean extends KeyPrimitive<NBTByte> {
    KeyBoolean(String name) {
        super(name, NBTByte.class, Tag.BYTE);
    }
}
