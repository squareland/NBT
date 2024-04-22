package net.minecraft.nbt;

import net.minecraft.nbt.factory.ByteSupplier;
import net.minecraft.nbt.factory.FloatSupplier;
import net.minecraft.nbt.factory.ShortSupplier;
import net.minecraft.nbt.key.*;

import java.util.UUID;
import java.util.function.*;

public interface CompoundRead extends CompoundReadObsolete {
    default boolean hasUnchecked(Key<?> key) {
        return hasKey(key.getName());
    }

    default boolean has(Key<?> key) {
        return hasKey(key.getName(), key.getTag());
    }

    default <N extends NBT<?>> N get(Key<? super N> key) {
        return (N) get(key.getName());
    }

    default NBT<?> getUnchecked(Key<?> key) {
        return get(key.getName());
    }

    default NBTCompound get(KeyCompound key) {
        return get(key, NBTCompound::new);
    }

    default NBTCompound get(KeyCompound key, Supplier<NBTCompound> fallback) {
        NBT<?> tag = getUnchecked(key);
        if (tag instanceof NBTCompound compound) {
            return compound;
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.COMPOUND, tag.getTag());
        }
        return fallback.get();
    }

    default <W extends CompoundWrapper> W get(KeyTyped<W> key) {
        return get(key, NBTCompound::new);
    }

    default <W extends CompoundWrapper> W get(KeyTyped<W> key, Supplier<NBTCompound> fallback) {
        NBT<?> tag = getUnchecked(key);
        if (tag instanceof NBTCompound compound) {
            return key.wrap(compound);
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.COMPOUND, tag.getTag());
        }
        return key.wrap(fallback.get());
    }

    default <W extends CompoundWrapper> W getOrNull(KeyTyped<W> key) {
        NBT<?> tag = get(key.getName());
        if (tag instanceof NBTCompound compound) {
            return key.wrap(compound);
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.COMPOUND, tag.getTag());
        }
        return null;
    }

    default <E extends Enum<E>> E get(KeyEnum<E> key) {
        return get(key, () -> key.wrap(new NBTByte((byte)0)));
    }

    default <E extends Enum<E>> E get(KeyEnum<E> key, Supplier<E> fallback) {
        NBT<?> tag = getUnchecked(key);
        if (tag instanceof NBTByte b) {
            return key.wrap(b);
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.BYTE, tag.getTag());
        }
        return fallback.get();
    }

    default <E extends Enum<E>> E getOrNull(KeyEnum<E> key) {
        NBT<?> tag = get(key.getName());
        if (tag instanceof NBTByte b) {
            return key.wrap(b);
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.BYTE, tag.getTag());
        }
        return null;
    }

    default NBTList get(KeyList key) {
        return get(key, NBTList::new);
    }

    default NBTList get(KeyList key, Supplier<NBTList> fallback) {
        NBT<?> tag = getUnchecked(key);
        if (tag instanceof NBTList list) {
            if (list.isEmpty() || list.getTagType() == key.getElementTag()) {
                return list;
            }
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.LIST, tag.getTag());
        }
        return fallback.get();
    }

    default <W extends CompoundWrapper> TypedList<W> get(KeyTypedList<W> key) {
        return get(key, NBTList::new);
    }

    default <W extends CompoundWrapper> TypedList<W> get(KeyTypedList<W> key, Supplier<NBTList> fallback) {
        NBT<?> tag = getUnchecked(key);
        if (tag instanceof NBTList list) {
            if (list.isEmpty() || list.getTagType() == key.getElementTag()) {
                return key.wrap(list);
            }
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.LIST, tag.getTag());
        }
        return key.wrap(fallback.get());
    }

    default <W extends CompoundWrapper> TypedList<W> getOrNull(KeyTypedList<W> key) {
        NBT<?> tag = getUnchecked(key);
        if (tag instanceof NBTList list) {
            if (list.isEmpty() || list.getTagType() == key.getElementTag()) {
                return key.wrap(list);
            }
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.LIST, tag.getTag());
        }
        return null;
    }

    default String get(KeyString key) {
        return get(key, "");
    }

    default String get(KeyString key, String fallback) {
        NBT<?> tag = getUnchecked(key);
        if (tag instanceof NBTString s) {
            return s.getString();
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.STRING, tag.getTag());
        }
        return fallback;
    }

    default UUID getUUID(KeyLong key) {
        return getUUID(key, null);
    }

    default UUID getUUID(KeyLong key, UUID fallback) {
        NBT<?> most = get(key.getName() + "Most");
        if (most instanceof NBTLong m) {
            NBT<?> least = get(key.getName() + "Least");
            if (least instanceof NBTLong l) {
                return new UUID(m.get(), l.get());
            }
            if (least != null) {
                throw new TagMismatchException(key, Tag.LONG, least.getTag());
            }
        }
        if (most != null) {
            throw new TagMismatchException(key, Tag.LONG, most.getTag());
        }
        return fallback;
    }

    default String get(KeyBoolean key, Supplier<String> fallback) {
        NBT<?> tag = getUnchecked(key);
        if (tag instanceof NBTString s) {
            return s.getString();
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.STRING, tag.getTag());
        }
        return fallback.get();
    }

    default boolean get(KeyBoolean key) {
        return get(key, false);
    }

    default boolean get(KeyBoolean key, boolean fallback) {
        NBT<?> tag = getUnchecked(key);
        if (tag instanceof NBTPrimitive<?> primitive) {
            return primitive.getByte() != 0;
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.BYTE, tag.getTag());
        }
        return fallback;
    }

    default boolean get(KeyBoolean key, BooleanSupplier fallback) {
        NBT<?> tag = getUnchecked(key);
        if (tag instanceof NBTPrimitive<?> primitive) {
            return primitive.getByte() != 0;
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.BYTE, tag.getTag());
        }
        return fallback.getAsBoolean();
    }

    default byte get(KeyByte key) {
        return get(key, (byte) 0);
    }

    default byte get(KeyByte key, byte fallback) {
        NBT<?> tag = getUnchecked(key);
        if (tag instanceof NBTPrimitive<?> primitive) {
            return primitive.getByte();
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.BYTE, tag.getTag());
        }
        return fallback;
    }

    default byte get(KeyByte key, ByteSupplier fallback) {
        NBT<?> tag = getUnchecked(key);
        if (tag instanceof NBTPrimitive<?> primitive) {
            return primitive.getByte();
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.BYTE, tag.getTag());
        }
        return fallback.getAsByte();
    }

    default short get(KeyShort key) {
        return get(key, (short) 0);
    }

    default short get(KeyShort key, short fallback) {
        NBT<?> tag = getUnchecked(key);
        if (tag instanceof NBTPrimitive<?> primitive) {
            return primitive.getShort();
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.SHORT, tag.getTag());
        }
        return fallback;
    }

    default short get(KeyShort key, ShortSupplier fallback) {
        NBT<?> tag = getUnchecked(key);
        if (tag instanceof NBTPrimitive<?> primitive) {
            return primitive.getShort();
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.SHORT, tag.getTag());
        }
        return fallback.getAsShort();
    }

    default int get(KeyInt key) {
        return get(key, 0);
    }

    default int get(KeyInt key, int fallback) {
        NBT<?> tag = getUnchecked(key);
        if (tag instanceof NBTPrimitive<?> primitive) {
            return primitive.getInt();
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.INT, tag.getTag());
        }
        return fallback;
    }

    default int get(KeyInt key, IntSupplier fallback) {
        NBT<?> tag = getUnchecked(key);
        if (tag instanceof NBTPrimitive<?> primitive) {
            return primitive.getInt();
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.INT, tag.getTag());
        }
        return fallback.getAsInt();
    }

    default long get(KeyLong key) {
        return get(key, 0L);
    }

    default long get(KeyLong key, long fallback) {
        NBT<?> tag = getUnchecked(key);
        if (tag instanceof NBTPrimitive<?> primitive) {
            return primitive.getLong();
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.LONG, tag.getTag());
        }
        return fallback;
    }

    default long get(KeyLong key, LongSupplier fallback) {
        NBT<?> tag = getUnchecked(key);
        if (tag instanceof NBTPrimitive<?> primitive) {
            return primitive.getLong();
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.LONG, tag.getTag());
        }
        return fallback.getAsLong();
    }

    default float get(KeyFloat key) {
        return get(key, 0F);
    }

    default float get(KeyFloat key, float fallback) {
        NBT<?> tag = getUnchecked(key);
        if (tag instanceof NBTPrimitive<?> primitive) {
            return primitive.getFloat();
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.FLOAT, tag.getTag());
        }
        return fallback;
    }

    default float get(KeyFloat key, FloatSupplier fallback) {
        NBT<?> tag = getUnchecked(key);
        if (tag instanceof NBTPrimitive<?> primitive) {
            return primitive.getFloat();
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.FLOAT, tag.getTag());
        }
        return fallback.getAsFloat();
    }

    default double get(KeyDouble key) {
        return get(key, 0D);
    }

    default double get(KeyDouble key, double fallback) {
        NBT<?> tag = getUnchecked(key);
        if (tag instanceof NBTPrimitive<?> primitive) {
            return primitive.getDouble();
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.DOUBLE, tag.getTag());
        }
        return fallback;
    }

    default double get(KeyDouble key, DoubleSupplier fallback) {
        NBT<?> tag = getUnchecked(key);
        if (tag instanceof NBTPrimitive<?> primitive) {
            return primitive.getDouble();
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.DOUBLE, tag.getTag());
        }
        return fallback.getAsDouble();
    }

    default byte[] get(KeyByteArray key) {
        return get(key, () -> new byte[0]);
    }

    default byte[] get(KeyByteArray key, byte[] fallback) {
        NBT<?> tag = getUnchecked(key);
        if (tag instanceof NBTByteArray a) {
            return a.get();
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.BYTE_ARRAY, tag.getTag());
        }
        return fallback;
    }

    default byte[] get(KeyByteArray key, Supplier<byte[]> fallback) {
        NBT<?> tag = getUnchecked(key);
        if (tag instanceof NBTByteArray a) {
            return a.get();
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.BYTE_ARRAY, tag.getTag());
        }
        return fallback.get();
    }

    default int[] get(KeyIntArray key) {
        return get(key, () -> new int[0]);
    }

    default int[] get(KeyIntArray key, int[] fallback) {
        NBT<?> tag = getUnchecked(key);
        if (tag instanceof NBTIntArray a) {
            return a.get();
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.INT_ARRAY, tag.getTag());
        }
        return fallback;
    }

    default int[] get(KeyIntArray key, Supplier<int[]> fallback) {
        NBT<?> tag = getUnchecked(key);
        if (tag instanceof NBTIntArray a) {
            return a.get();
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.INT_ARRAY, tag.getTag());
        }
        return fallback.get();
    }

    default long[] get(KeyLongArray key) {
        return get(key, () -> new long[0]);
    }

    default long[] get(KeyLongArray key, long[] fallback) {
        NBT<?> tag = getUnchecked(key);
        if (tag instanceof NBTLongArray a) {
            return a.get();
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.LONG_ARRAY, tag.getTag());
        }
        return fallback;
    }

    default long[] get(KeyLongArray key, Supplier<long[]> fallback) {
        NBT<?> tag = getUnchecked(key);
        if (tag instanceof NBTLongArray a) {
            return a.get();
        }
        if (tag != null) {
            throw new TagMismatchException(key, Tag.LONG_ARRAY, tag.getTag());
        }
        return fallback.get();
    }
}
