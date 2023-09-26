package net.minecraft.nbt;

import java.util.ArrayList;
import java.util.List;

public class Node<T> {
    private final boolean isStart;
    private final List<Node<T>> children = new ArrayList<>();
    private Node<T> parent;
    private String name;
    private T element;
    private boolean drawChildren;

    public Node(String name, T element) {
        this.isStart = true;
        this.name = name;
        this.element = element;
    }

    public Node(Node<T> parent, String name, T element) {
        this.parent = parent;
        this.isStart = parent != null;
        this.name = name;
        this.element = element;
    }

    public boolean shouldDrawChildren() {
        return this.drawChildren;
    }

    public void setDrawChildren(boolean draw) {
        this.drawChildren = draw;
    }

    public void addChild(Node<T> n) {
        this.children.add(n);
    }

    public boolean removeChild(Node<T> n) {
        return this.children.remove(n);
    }

    public List<Node<T>> getChildren() {
        return this.children;
    }

    public Node<T> getParent() {
        return this.parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T get() {
        return this.element;
    }

    public void set(T element) {
        this.element = element;
    }

    public String display(boolean format) {
        String value = switch (element) {
            case NBTCompound c -> "(TagCompound)";
            case NBTList l -> "(TagList)";
            default -> element.toString();
        };
        return name == null || name.isEmpty()
            ? value
            : name + ": " + value + (format ? "\u00a7r" : "");

    }

    @Override
    public String toString() {
        return "" + this.element;
    }

    public boolean hasChildren() {
        return this.children.size() > 0;
    }

    public boolean hasParent() {
        return this.parent != null;
    }
}
