package net.minecraft.nbt;

import net.minecraft.nbt.key.Key;

public class TagMismatchException extends RuntimeException {
    public final Object key;
    public final Tag expected;
    public final Tag found;

    public TagMismatchException(String key, Tag expected, Tag found) {
        super("NBT tag mismatch for key '" + key + "'. Expected " + expected + " but found " + found);
        this.key = key;
        this.expected = expected;
        this.found = found;
    }

    public TagMismatchException(Key<?> key, Tag expected, Tag found) {
        super("NBT tag mismatch for key '" + key.getName() + "'. Expected " + expected + " but found " + found);
        this.key = key;
        this.expected = expected;
        this.found = found;
    }
}
