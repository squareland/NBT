package net.minecraft.nbt;

import net.minecraft.nbt.factory.ByteSupplier;
import net.minecraft.nbt.factory.FloatSupplier;
import net.minecraft.nbt.factory.ShortSupplier;

import java.util.UUID;
import java.util.function.*;

public sealed interface CompoundRead permits NBTCompound {
    NBT get(String key);

    Tag getTagId(String key);

    boolean hasKey(String key);

    default boolean hasKey(String key, Tag tag) {
        Tag type = getTagId(key);
        return type == tag;
    }

    default boolean hasNumericKey(String key) {
        Tag type = getTagId(key);
        return type == Tag.BYTE || type == Tag.SHORT || type == Tag.INT || type == Tag.LONG || type == Tag.FLOAT || type == Tag.DOUBLE;
    }

    default UUID getUniqueId(String key) {
        return new UUID(getLong(key + "Most"), getLong(key + "Least"));
    }

    default boolean hasUniqueId(String key) {
        return hasNumericKey(key + "Most") && hasNumericKey(key + "Least");
    }

    default byte getByte(String key) {
        return getByte(key, () -> (byte) 0);
    }

    default byte getByte(String key, ByteSupplier fallback) {
        NBT tag = get(key);
        if (tag instanceof NBTPrimitive primitive) {
            return primitive.getByte();
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.BYTE, tag.getTag());
        }
        return fallback.getAsByte();
    }

    default short getShort(String key) {
        return getShort(key, () -> (short) 0);
    }

    default short getShort(String key, ShortSupplier fallback) {
        NBT tag = get(key);
        if (tag instanceof NBTPrimitive primitive) {
            return primitive.getShort();
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.SHORT, tag.getTag());
        }
        return fallback.getAsShort();
    }

    default int getInteger(String key) {
        return getInteger(key, () -> 0);
    }

    default int getInteger(String key, IntSupplier fallback) {
        NBT tag = get(key);
        if (tag instanceof NBTPrimitive primitive) {
            return primitive.getInt();
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.INT, tag.getTag());
        }
        return fallback.getAsInt();
    }

    default long getLong(String key) {
        return getLong(key, () -> 0);
    }

    default long getLong(String key, LongSupplier fallback) {
        NBT tag = get(key);
        if (tag instanceof NBTPrimitive primitive) {
            return primitive.getLong();
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.LONG, tag.getTag());
        }
        return fallback.getAsLong();
    }

    default float getFloat(String key) {
        return getFloat(key, () -> 0);
    }

    default float getFloat(String key, FloatSupplier fallback) {
        NBT tag = get(key);
        if (tag instanceof NBTPrimitive primitive) {
            return primitive.getFloat();
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.FLOAT, tag.getTag());
        }
        return fallback.getAsFloat();
    }

    default double getDouble(String key) {
        return getDouble(key, () -> 0);
    }

    default double getDouble(String key, DoubleSupplier fallback) {
        NBT tag = get(key);
        if (tag instanceof NBTPrimitive primitive) {
            return primitive.getDouble();
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.DOUBLE, tag.getTag());
        }
        return fallback.getAsDouble();
    }

    default String getString(String key) {
        return getString(key, String::new);
    }

    default String getString(String key, Supplier<String> fallback) {
        NBT tag = get(key);
        if (tag instanceof NBTString string) {
            return string.getString();
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.STRING, tag.getTag());
        }
        return fallback.get();
    }

    default byte[] getByteArray(String key) {
        return getByteArray(key, () -> new byte[0]);
    }

    default byte[] getByteArray(String key, Supplier<byte[]> fallback) {
        NBT tag = get(key);
        if (tag instanceof NBTByteArray array) {
            return array.getByteArray();
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.BYTE_ARRAY, tag.getTag());
        }
        return fallback.get();
    }

    default int[] getIntArray(String key) {
        return getIntArray(key, () -> new int[0]);
    }

    default int[] getIntArray(String key, Supplier<int[]> fallback) {
        NBT tag = get(key);
        if (tag instanceof NBTIntArray array) {
            return array.getIntArray();
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.INT_ARRAY, tag.getTag());
        }
        return fallback.get();
    }

    default NBTCompound getCompound(String key) {
        return getCompound(key, NBTCompound::new);
    }

    default NBTCompound getCompound(String key, Supplier<NBTCompound> fallback) {
        NBT tag = get(key);
        if (tag instanceof NBTCompound compound) {
            return compound;
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.COMPOUND, tag.getTag());
        }
        return fallback.get();
    }

    default NBTList getTagList(String key, Tag type) {
        return getTagList(key, type, NBTList::new);
    }

    default NBTList getTagList(String key, Tag type, Supplier<NBTList> fallback) {
        NBT tag = get(key);
        if (tag instanceof NBTList list) {
            if (list.isEmpty() || list.getTagType() == type) {
                return list;
            }
        }
        return fallback.get();
    }

    default boolean getBoolean(String key) {
        return getBoolean(key, () -> false);
    }

    default boolean getBoolean(String key, BooleanSupplier fallback) {
        NBT tag = get(key);
        if (tag instanceof NBTPrimitive primitive) {
            return primitive.getByte() != 0;
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.BYTE, tag.getTag());
        }
        return fallback.getAsBoolean();
    }
}
