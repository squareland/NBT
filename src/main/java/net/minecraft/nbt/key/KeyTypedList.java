package net.minecraft.nbt.key;

import net.minecraft.nbt.CompoundWrapper;
import net.minecraft.nbt.NBTCompound;
import net.minecraft.nbt.NBTList;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TypedList;

import java.util.function.Function;

public class KeyTypedList<W extends CompoundWrapper> extends KeyList {
    private final Class<W> wrappedType;
    private final Function<NBTCompound, W> factory;

    KeyTypedList(String name, Class<W> wrappedType, Function<NBTCompound, W> factory) {
        super(name, Tag.COMPOUND);
        this.wrappedType = wrappedType;
        this.factory = factory;
    }

    public Class<W> getWrappedType() {
        return wrappedType;
    }

    public TypedList<W> bake(NBTList tag) {
        return tag == null ? null : new TypedList<>(wrappedType, factory, tag);
    }
}
