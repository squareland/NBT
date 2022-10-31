package net.minecraft.nbt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class NBTTree {
    private final String name;
    private final NBTCompound baseTag;
    private Node<NBT<?>> root;

    public NBTTree(String name, NBTCompound tag) {
        this.name = name;
        this.baseTag = tag;
        this.construct();
    }

    public static String repeat(String c, int i) {
        return String.valueOf(c).repeat(Math.max(0, i));
    }

    public Node<NBT<?>> getRoot() {
        return this.root;
    }

    public boolean canDelete(Node<NBT<?>> node) {
        return node != this.root;
    }

    public boolean delete(Node<NBT<?>> node) {
        return node != null && node != this.root && this.deleteNode(node, this.root);
    }

    private boolean deleteNode(Node<NBT<?>> toDelete, Node<NBT<?>> cur) {
        Iterator<Node<NBT<?>>> it = cur.getChildren().iterator();

        while (it.hasNext()) {
            Node<NBT<?>> child = it.next();
            if (child == toDelete) {
                it.remove();
                return true;
            }
            if (deleteNode(toDelete, child)) {
                return true;
            }
        }
        return false;
    }

    private void construct() {
        NBTCompound tag = this.baseTag.copy();
        this.root = new Node<>(name, tag);
        this.addChildrenToTree(this.root, tag);
        this.sort(this.root);
    }

    public void sort(Node<NBT<?>> node) {
        node.getChildren().sort(new NBTNodeSorter());

        for (Node<NBT<?>> c : node.getChildren()) {
            this.sort(c);
        }
    }

    public void addChildrenToTree(Node<NBT<?>> parent, NBT<?> tag) {
        if (tag instanceof NBTCompound c) {
            for (Entry<String, NBT<?>> entry : c.entrySet()) {
                NBT<?> base = entry.getValue();
                Node<NBT<?>> child = new Node<>(parent, entry.getKey(), base);
                parent.addChild(child);
                this.addChildrenToTree(child, base);
            }
        } else if (tag instanceof NBTList list) {
            for (int i = 0; i < list.size(); ++i) {
                NBT<?> base = list.get(i);
                Node<NBT<?>> child = new Node<>(parent, "" + i, base);
                parent.addChild(child);
                this.addChildrenToTree(child, base);
            }
        }

    }

    public NBTCompound toNBTTagCompound() {
        NBTCompound tag = new NBTCompound();
        this.addChildrenToTag(this.root, tag);
        return tag;
    }

    public void addChildrenToTag(Node<NBT<?>> parent, NBTCompound tag) {
        for (Node<NBT<?>> child : parent.getChildren()) {
            NBT<?> base = child.get();
            if (base instanceof NBTCompound c) {
                NBTCompound newTag = new NBTCompound();
                this.addChildrenToTag(child, newTag);
                tag.set(child.getName(), newTag);
            } else if (base instanceof NBTList) {
                NBTList list = new NBTList();
                this.addChildrenToList(child, list);
                tag.set(child.getName(), list);
            } else {
                tag.set(child.getName(), base.copy());
            }
        }

    }

    public void addChildrenToList(Node<NBT<?>> parent, NBTList list) {
        for (Node<NBT<?>> child : parent.getChildren()) {
            NBT<?> base = child.get();
            if (base instanceof NBTCompound) {
                NBTCompound newTag = new NBTCompound();
                this.addChildrenToTag(child, newTag);
                list.push(newTag);
            } else if (base instanceof NBTList) {
                NBTList newList = new NBTList();
                this.addChildrenToList(child, newList);
                list.push(newList);
            } else {
                list.push(base.copy());
            }
        }

    }

    public void print() {
        print(root, 0);
    }

    private void print(Node<NBT<?>> n, int i) {
        System.out.println(repeat("\t", i) + getNBTName(n));

        for (Node<NBT<?>> child : n.getChildren()) {
            print(child, i + 1);
        }

    }

    public List<String> toStrings() {
        List<String> s = new ArrayList<>();
        this.toStrings(s, this.root, 0);
        return s;
    }

    private void toStrings(List<String> s, Node<NBT<?>> n, int i) {
        s.add(repeat("   ", i) + getNBTName(n));
        for (Node<NBT<?>> child : n.getChildren()) {
            this.toStrings(s, child, i + 1);
        }
    }

    private static String getNBTName(Node<NBT<?>> node) {
        NBT<?> obj = node.get();
        String s = obj instanceof NBTCompound ? "(TagCompound)" : (obj instanceof NBTList ? "(TagList)" : "" + obj);
        String name = node.getName();
        return name == null || name.isEmpty() ? "" + s : name + ": " + s;
    }
}
