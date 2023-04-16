package net.minecraft.nbt;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;

@Deprecated
public interface CompoundWriteObsolete {
    @Deprecated
    void set(String key, NBT<?> value);

    @Deprecated
    NBT<?> replace(String key, NBT<?> value);

    @Deprecated
    NBT<?> merge(String key, NBT<?> value, BiFunction<? super NBT<?>, ? super NBT<?>, ? extends NBT<?>> mapping);

    @Deprecated
    NBT<?> compute(String key, BiFunction<String, ? super NBT<?>, ? extends NBT<?>> mapping);

    @Deprecated
    NBT<?> remove(String key);

    @Deprecated
    default void setByte(String key, byte value) {
        set(key, new NBTByte(value));
    }

    @Deprecated
    default void setShort(String key, short value) {
        set(key, new NBTShort(value));
    }

    @Deprecated
    default void setInteger(String key, int value) {
        set(key, new NBTInt(value));
    }

    @Deprecated
    default void setLong(String key, long value) {
        set(key, new NBTLong(value));
    }

    @Deprecated
    default void setUniqueId(String key, UUID value) {
        setLong(key + "Most", value.getMostSignificantBits());
        setLong(key + "Least", value.getLeastSignificantBits());
    }

    @Deprecated
    default void setFloat(String key, float value) {
        set(key, new NBTFloat(value));
    }

    @Deprecated
    default void setDouble(String key, double value) {
        set(key, new NBTDouble(value));
    }

    @Deprecated
    default void setString(String key, String value) {
        set(key, new NBTString(value));
    }

    @Deprecated
    default void setByteArray(String key, byte[] value) {
        set(key, new NBTByteArray(value));
    }

    @Deprecated
    default void setByteArray(String key, List<Byte> value) {
        set(key, new NBTByteArray(value));
    }

    @Deprecated
    default void setByteArray(String key, ByteBuffer value) {
        set(key, new NBTByteArray(value));
    }

    @Deprecated
    default void setIntArray(String key, int[] value) {
        set(key, new NBTIntArray(value));
    }

    @Deprecated
    default void setBoolean(String key, boolean value) {
        setByte(key, (byte) (value ? 1 : 0));
    }

}
