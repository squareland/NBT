package net.minecraft.nbt;

import net.minecraft.nbt.key.*;

public sealed interface CompoundWrite extends CompoundWriteObsolete permits NBTCompound {
    default <T, N extends NBT<T>> T remove(Key<N> key) {
        NBT<?> tag = remove(key.getName());
        return (T) tag.get();
    }

    default <T, N extends NBT<T>> void drop(Key<N> key) {
        remove(key.getName());
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

    default byte increment(KeyByte key, byte quantity) {
        NBT<?> tag = compute(key.getName(), (k, old) -> {
            if (old instanceof NBTPrimitive<?> primitive) {
                return new NBTByte((byte) (primitive.getByte() + quantity));
            }
            if (old != null) {
                throw new TagMismatchException(k, Tag.BYTE, old.getTag());
            }
            return new NBTByte(quantity);
        });
        if (tag instanceof NBTPrimitive<?> primitive) {
            return primitive.getByte();
        } else {
            throw new TagMismatchException(key, Tag.BYTE, tag == null ? null : tag.getTag());
        }
    }

    default byte decrement(KeyByte key, byte quantity) {
        return increment(key, (byte) -quantity);
    }

    default short increment(KeyShort key, short quantity) {
        NBT<?> tag = compute(key.getName(), (k, old) -> {
            if (old instanceof NBTPrimitive<?> primitive) {
                return new NBTShort((short) (primitive.getShort() + quantity));
            }
            if (old != null) {
                throw new TagMismatchException(k, Tag.SHORT, old.getTag());
            }
            return new NBTShort(quantity);
        });
        if (tag instanceof NBTPrimitive<?> primitive) {
            return primitive.getShort();
        } else {
            throw new TagMismatchException(key, Tag.SHORT, tag == null ? null : tag.getTag());
        }
    }

    default short decrement(KeyShort key, short quantity) {
        return increment(key, (short) -quantity);
    }

    default int increment(KeyInt key, int quantity) {
        NBT<?> tag = compute(key.getName(), (k, old) -> {
            if (old instanceof NBTPrimitive<?> primitive) {
                return new NBTInt(primitive.getInt() + quantity);
            }
            if (old != null) {
                throw new TagMismatchException(k, Tag.INT, old.getTag());
            }
            return new NBTInt(quantity);
        });
        if (tag instanceof NBTPrimitive<?> primitive) {
            return primitive.getInt();
        } else {
            throw new TagMismatchException(key, Tag.INT, tag == null ? null : tag.getTag());
        }
    }

    default int decrement(KeyInt key, int quantity) {
        return increment(key, -quantity);
    }

    default long increment(KeyLong key, long quantity) {
        NBT<?> tag = compute(key.getName(), (k, old) -> {
            if (old instanceof NBTPrimitive<?> primitive) {
                return new NBTLong(primitive.getLong() + quantity);
            }
            if (old != null) {
                throw new TagMismatchException(k, Tag.LONG, old.getTag());
            }
            return new NBTLong(quantity);
        });
        if (tag instanceof NBTPrimitive<?> primitive) {
            return primitive.getLong();
        } else {
            throw new TagMismatchException(key, Tag.LONG, tag == null ? null : tag.getTag());
        }
    }

    default long decrement(KeyLong key, long quantity) {
        return increment(key, -quantity);
    }

    default float increment(KeyFloat key, float quantity) {
        NBT<?> tag = compute(key.getName(), (k, old) -> {
            if (old instanceof NBTPrimitive<?> primitive) {
                return new NBTFloat(primitive.getFloat() + quantity);
            }
            if (old != null) {
                throw new TagMismatchException(k, Tag.FLOAT, old.getTag());
            }
            return new NBTFloat(quantity);
        });
        if (tag instanceof NBTPrimitive<?> primitive) {
            return primitive.getFloat();
        } else {
            throw new TagMismatchException(key, Tag.FLOAT, tag == null ? null : tag.getTag());
        }
    }

    default float decrement(KeyFloat key, float quantity) {
        return increment(key, -quantity);
    }

    default double increment(KeyDouble key, double quantity) {
        NBT<?> tag = compute(key.getName(), (k, old) -> {
            if (old instanceof NBTPrimitive<?> primitive) {
                return new NBTDouble(primitive.getDouble() + quantity);
            }
            if (old != null) {
                throw new TagMismatchException(k, Tag.DOUBLE, old.getTag());
            }
            return new NBTDouble(quantity);
        });
        if (tag instanceof NBTPrimitive<?> primitive) {
            return primitive.getDouble();
        } else {
            throw new TagMismatchException(key, Tag.DOUBLE, tag == null ? null : tag.getTag());
        }
    }

    default double decrement(KeyDouble key, double quantity) {
        return increment(key, -quantity);
    }
}
