package net.minecraft.nbt.key;

import net.minecraft.nbt.NBTByte;

public class KeyEnum<E extends Enum<E>> extends KeyByte {
    private final Class<E> wrappedType;
    private final E[] variants;

    KeyEnum(String name, Class<E> wrappedType) {
        super(name);
        this.wrappedType = wrappedType;
        this.variants = wrappedType.getEnumConstants();
    }

    public Class<E> getWrappedType() {
        return wrappedType;
    }

    public E wrap(NBTByte tag) {
        return tag == null ? null : variants[tag.get()];
    }
}
