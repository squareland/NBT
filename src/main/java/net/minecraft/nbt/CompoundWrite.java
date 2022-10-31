package net.minecraft.nbt;

import net.minecraft.nbt.key.*;

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

    default void set(KeyByteArray key, byte[] value) {
        setUnchecked(key, new NBTByteArray(value));
    }

    default void set(KeyIntArray key, int[] value) {
        setUnchecked(key, new NBTIntArray(value));
    }

    default void set(KeyCompound key, NBTCompound value) {
        setUnchecked(key, value);
    }

    default void set(KeyList key, NBTList value) {
        setUnchecked(key, value);
    }

    default void set(KeyBoolean key, boolean value) {
        setUnchecked(key, new NBTByte((byte) (value ? 1 : 0)));
    }

    default void set(KeyByte key, byte value) {
        setUnchecked(key, new NBTByte(value));
    }

    default void set(KeyShort key, short value) {
        setUnchecked(key, new NBTShort(value));
    }

    default void set(KeyInt key, int value) {
        setUnchecked(key, new NBTInt(value));
    }

    default void set(KeyLong key, long value) {
        setUnchecked(key, new NBTLong(value));
    }

    default void set(KeyFloat key, float value) {
        setUnchecked(key, new NBTFloat(value));
    }

    default void set(KeyDouble key, double value) {
        setUnchecked(key, new NBTDouble(value));
    }
}
