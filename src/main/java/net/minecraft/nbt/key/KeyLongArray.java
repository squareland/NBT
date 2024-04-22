package net.minecraft.nbt.key;

import net.minecraft.nbt.NBTLongArray;
import net.minecraft.nbt.Tag;

public class KeyLongArray extends Key<NBTLongArray> {
    KeyLongArray(String name) {
        super(name, NBTLongArray.class, Tag.LONG_ARRAY);
    }
}
