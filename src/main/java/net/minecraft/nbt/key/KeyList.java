package net.minecraft.nbt.key;

import net.minecraft.nbt.NBTList;
import net.minecraft.nbt.Tag;

public class KeyList extends Key<NBTList> {
    KeyList(String name) {
        super(name, NBTList.class, Tag.LIST);
    }
}
