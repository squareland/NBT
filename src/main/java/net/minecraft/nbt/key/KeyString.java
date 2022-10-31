package net.minecraft.nbt.key;

import net.minecraft.nbt.NBTString;
import net.minecraft.nbt.Tag;

public class KeyString extends Key<NBTString> {
    KeyString(String name) {
        super(name, NBTString.class, Tag.STRING);
    }
}
