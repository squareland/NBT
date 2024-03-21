package net.minecraft.nbt.key;

import net.minecraft.nbt.CompoundWrapper;
import net.minecraft.nbt.NBTCompound;

import java.util.function.Function;

public class KeyTyped<W extends CompoundWrapper> extends KeyCompound {
    private final Class<W> wrappedType;
    private final Function<NBTCompound, W> factory;

    KeyTyped(String name, Class<W> wrappedType, Function<NBTCompound, W> factory) {
        super(name);
        this.wrappedType = wrappedType;
        this.factory = factory;
    }

    public Class<W> getWrappedType() {
        return wrappedType;
    }

    public W bake(NBTCompound tag) {
        return tag == null ? null : factory.apply(tag);
    }
}
