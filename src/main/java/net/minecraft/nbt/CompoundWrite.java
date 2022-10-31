package net.minecraft.nbt;

import net.minecraft.nbt.key.Key;
import net.minecraft.nbt.key.KeyInt;
import net.minecraft.nbt.key.KeyString;

public sealed interface CompoundWrite extends CompoundWriteObsolete permits NBTCompound {
    default <T, N extends NBT<T>> T remove(Key<N> key) {
        NBT<?> tag = remove(key.getName());
        return (T) tag.get();
    }

    default void setUnchecked(Key<?> key, NBT<?> value) {
        set(key.getName(), value);
    }

    default void set(KeyString key, String value) {
        setUnchecked(key, new NBTString(value));
    }

    default void set(KeyInt key, int value) {
        setUnchecked(key, new NBTInt(value));
    }
}
