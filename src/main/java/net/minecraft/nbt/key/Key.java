package net.minecraft.nbt.key;

import net.minecraft.nbt.*;

import java.util.function.Function;

public class Key<N extends NBT<?>> {
    private final String name;
    private final Class<N> type;
    private final Tag tag;

    Key(String name, Class<N> type, Tag tag) {
        this.name = name;
        this.type = type;
        this.tag = tag;
    }

    public static KeyDouble ofDouble(String name) {
        return new KeyDouble(name);
    }

    public static KeyFloat ofFloat(String name) {
        return new KeyFloat(name);
    }

    public static KeyInt ofInt(String name) {
        return new KeyInt(name);
    }

    public static KeyLong ofLong(String name) {
        return new KeyLong(name);
    }

    public static KeyByte ofByte(String name) {
        return new KeyByte(name);
    }

    public static KeyBoolean ofBoolean(String name) {
        return new KeyBoolean(name);
    }

    public static KeyShort ofShort(String name) {
        return new KeyShort(name);
    }

    public static KeyString ofString(String name) {
        return new KeyString(name);
    }

    public static KeyByteArray ofByteArray(String name) {
        return new KeyByteArray(name);
    }

    public static KeyIntArray ofIntArray(String name) {
        return new KeyIntArray(name);
    }

    public static KeyLongArray ofLongArray(String name) {
        return new KeyLongArray(name);
    }

    public static KeyCompound ofCompound(String name) {
        return new KeyCompound(name);
    }

    public static KeyList ofList(String name, Tag elementTag) {
        return new KeyList(name, elementTag);
    }

    public static <W extends CompoundWrapper> KeyTyped<W> ofType(String name, Class<W> wrappedType, Function<NBTCompound, W> factory) {
        return new KeyTyped<>(name, wrappedType, factory);
    }

    public static <W extends CompoundWrapper> KeyTypedList<W> ofTypeList(String name, Class<W> wrappedType, Function<NBTCompound, W> factory) {
        return new KeyTypedList<>(name, wrappedType, factory);
    }

    public String getName() {
        return name;
    }

    public Class<N> getType() {
        return type;
    }

    public Tag getTag() {
        return tag;
    }
}
