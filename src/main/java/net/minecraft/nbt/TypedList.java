package net.minecraft.nbt;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

public class TypedList<W extends CompoundWrapper> implements Iterable<W> {
    private final Class<W> wrappedType;
    private final Function<NBTCompound, W> factory;
    final NBTList list;

    public TypedList(Class<W> wrappedType, Function<NBTCompound, W> factory, NBTList list) {
        this.wrappedType = wrappedType;
        this.factory = factory;
        this.list = list;
    }

    public Class<W> getWrappedType() {
        return wrappedType;
    }

    private W bake(NBTCompound tag) {
        return factory.apply(tag);
    }

    public W remove(int index) {
        NBT<?> tag = list.remove(index);
        if (tag instanceof NBTCompound compound) {
            return bake(compound);
        }
        if (tag != null) {
            throw new TagMismatchException("list[" + index + "]", Tag.COMPOUND, tag.getTag());
        }
        return bake(new NBTCompound());
    }

    public void push(W value) {
        list.push(value.tag);
    }

    public void set(int index, W value) {
        list.set(index, value.tag);
    }

    public W get(int index) {
        return bake(list.getCompound(index));
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public void clear() {
        list.clear();
    }

    public TypedList<W> copy() {
        return new TypedList<>(wrappedType, factory, list.copy());
    }

    @Override
    public Iterator<W> iterator() {
        return new Iterator<>() {
            int current;

            @Override
            public boolean hasNext() {
                return current < size();
            }

            @Override
            public W next() {
                return get(current++);
            }
        };
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof TypedList<?> wl && Objects.equals(list, wl.list);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(list);
    }
}
