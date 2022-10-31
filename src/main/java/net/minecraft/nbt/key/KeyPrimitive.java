package net.minecraft.nbt.key;

import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.Tag;

public class KeyPrimitive<N extends NBTPrimitive<?>> extends Key<N> {
    KeyPrimitive(String name, Class<N> type, Tag tag) {
        super(name, type, tag);
    }
}
