package net.minecraft.nbt.key;

import net.minecraft.nbt.NBTDouble;
import net.minecraft.nbt.Tag;

public class KeyDouble extends KeyPrimitive<NBTDouble> {
    KeyDouble(String name) {
        super(name, NBTDouble.class, Tag.DOUBLE);
    }
}
