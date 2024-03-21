package net.minecraft.nbt.key;

import net.minecraft.nbt.CompoundWrapper;
import net.minecraft.nbt.NBTCompound;

import java.util.function.Function;

public class KeyTyped<W extends CompoundWrapper> extends KeyCompound {
    private final Class<W> wrapperType;
    private final Function<NBTCompound, W> factory;

    KeyTyped(String name, Class<W> wrapperType, Function<NBTCompound, W> factory) {
        super(name);
        this.wrapperType = wrapperType;
        this.factory = factory;
    }

    public Class<W> getWrapperType() {
        return wrapperType;
    }

    public W bake(NBTCompound tag) {
        return tag == null ? null : factory.apply(tag);
    }
}
