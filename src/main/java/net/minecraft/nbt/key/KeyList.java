package net.minecraft.nbt.key;

import net.minecraft.nbt.NBTList;
import net.minecraft.nbt.Tag;

public class KeyList extends Key<NBTList> {
    private final Tag elementTag;

    KeyList(String name, Tag elementTag) {
        super(name, NBTList.class, Tag.LIST);
        this.elementTag = elementTag;
    }

    public Tag getElementTag() {
        return elementTag;
    }
}
