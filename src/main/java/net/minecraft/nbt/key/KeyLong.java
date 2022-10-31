package net.minecraft.nbt.key;

import net.minecraft.nbt.NBTLong;
import net.minecraft.nbt.Tag;

public class KeyLong extends KeyPrimitive<NBTLong> {
    KeyLong(String name) {
        super(name, NBTLong.class, Tag.LONG);
    }
}
