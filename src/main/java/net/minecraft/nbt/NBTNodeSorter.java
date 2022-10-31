package net.minecraft.nbt;

import java.util.Comparator;

public class NBTNodeSorter implements Comparator<Node<NBT<?>>> {
    @Override
    public int compare(Node<NBT<?>> a, Node<NBT<?>> b) {
        NBT<?> n1 = a.get();
        NBT<?> n2 = b.get();
        if (!(n1 instanceof NBTCompound) && !(n1 instanceof NBTList)) {
            if (!(n2 instanceof NBTCompound) && !(n2 instanceof NBTList)) {
                int dif = n1.getTag().ordinal() - n2.getTag().ordinal();
                return dif == 0 ? a.getName().compareTo(b.getName()) : dif;
            } else {
                return -1;
            }
        } else if (!(n2 instanceof NBTCompound) && !(n2 instanceof NBTList)) {
            return 1;
        } else {
            int dif = n1.getTag().ordinal() - n2.getTag().ordinal();
            return dif == 0 ? a.getName().compareTo(b.getName()) : dif;
        }
    }
}
