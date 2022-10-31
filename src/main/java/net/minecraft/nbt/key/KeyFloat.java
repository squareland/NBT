package net.minecraft.nbt.key;

import net.minecraft.nbt.NBTFloat;
import net.minecraft.nbt.Tag;

public class KeyFloat extends KeyPrimitive<NBTFloat> {
    KeyFloat(String name) {
        super(name, NBTFloat.class, Tag.FLOAT);
    }
}
