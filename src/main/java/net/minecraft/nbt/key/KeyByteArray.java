package net.minecraft.nbt.key;

import net.minecraft.nbt.NBTByteArray;
import net.minecraft.nbt.Tag;

public class KeyByteArray extends Key<NBTByteArray> {
    KeyByteArray(String name) {
        super(name, NBTByteArray.class, Tag.BYTE_ARRAY);
    }
}
