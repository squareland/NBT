package net.minecraft.nbt;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.UUID;

public sealed interface CompoundWrite permits NBTCompound {
    void set(String key, NBT value);

    NBT remove(String key);

    default void setByte(String key, byte value) {
        set(key, new NBTByte(value));
    }

    default void setShort(String key, short value) {
        set(key, new NBTShort(value));
    }

    default void setInteger(String key, int value) {
        set(key, new NBTInt(value));
    }

    default void setLong(String key, long value) {
        set(key, new NBTLong(value));
    }

    default void setUniqueId(String key, UUID value) {
        setLong(key + "Most", value.getMostSignificantBits());
        setLong(key + "Least", value.getLeastSignificantBits());
    }

    default void setFloat(String key, float value) {
        set(key, new NBTFloat(value));
    }

    default void setDouble(String key, double value) {
        set(key, new NBTDouble(value));
    }

    default void setString(String key, String value) {
        set(key, new NBTString(value));
    }

    default void setByteArray(String key, byte[] value) {
        set(key, new NBTByteArray(value));
    }

    default void setByteArray(String key, List<Byte> value) {
        set(key, new NBTByteArray(value));
    }

    default void setByteArray(String key, ByteBuffer value) {
        set(key, new NBTByteArray(value));
    }

    default void setIntArray(String key, int[] value) {
        set(key, new NBTIntArray(value));
    }

    default void setBoolean(String key, boolean value) {
        setByte(key, (byte) (value ? 1 : 0));
    }

}
