package net.minecraft.nbt;

public class TagMismatchException extends RuntimeException {
    public final String key;
    public final Tag expected;
    public final Tag found;

    public TagMismatchException(String key, Tag expected, Tag found) {
        super("NBT tag mismatch for key '" + key + "'. Expected " + expected + " but found " + found);
        this.key = key;
        this.expected = expected;
        this.found = found;
    }
}
