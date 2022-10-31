package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

public final class NBTString extends NBT<String> {
    private final String data;

    public NBTString() {
        this("");
    }

    public NBTString(String data) {
        Objects.requireNonNull(data, "Null string not allowed");
        this.data = data;
    }

    public NBTString(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(288L);
        this.data = input.readUTF();
        NBTSizeTracker.readUTF(sizeTracker, data); // Forge: Correctly read String length including header.
    }

    public static String quoteAndEscape(String string) {
        StringBuilder result = new StringBuilder("\"");

        for (int i = 0; i < string.length(); ++i) {
            char c = string.charAt(i);

            if (c == '\\' || c == '"') {
                result.append('\\');
            }

            result.append(c);
        }

        return result.append('"').toString();
    }

    @Override
    void write(DataOutput output) throws IOException {
        output.writeUTF(this.data);
    }

    @Override
    public Tag getTag() {
        return Tag.STRING;
    }

    public String toString() {
        return quoteAndEscape(this.data);
    }

    @Override
    public NBTString copy() {
        return this;
    }

    @Override
    public String get() {
        return data;
    }

    @Override
    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    @Override
    public boolean equals(Object other) {
        if (!super.equals(other)) {
            return false;
        } else {
            NBTString string = (NBTString) other;
            return this.data == null && string.data == null || Objects.equals(this.data, string.data);
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ this.data.hashCode();
    }

    @Override
    public String getString() {
        return this.data;
    }
}