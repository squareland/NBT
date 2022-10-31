package net.minecraft.nbt.key;

import net.minecraft.nbt.NBTIntArray;
import net.minecraft.nbt.Tag;

public class KeyIntArray extends Key<NBTIntArray> {
    KeyIntArray(String name) {
        super(name, NBTIntArray.class, Tag.INT_ARRAY);
    }
}
