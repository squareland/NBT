package net.minecraft.nbt;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CompressedStreamTools {
    public static NBTCompound readCompressed(InputStream is) throws IOException {
        try (DataInputStream stream = new DataInputStream(new BufferedInputStream(new GZIPInputStream(is)))) {
            return read(stream, NBTSizeTracker.INFINITE);
        }
    }

    public static void writeCompressed(NBTCompound compound, OutputStream outputStream) throws IOException {
        try (DataOutputStream stream = new DataOutputStream(new BufferedOutputStream(new GZIPOutputStream(outputStream)))) {
            write(compound, stream);
        }
    }

    public static void safeWrite(NBTCompound compound, File file) throws IOException {
        File tmp = new File(file.getAbsolutePath() + "_tmp");

        if (tmp.exists()) {
            tmp.delete();
        }

        write(compound, tmp);

        if (file.exists()) {
            file.delete();
        }

        if (file.exists()) {
            throw new IOException("Failed to delete " + file);
        } else {
            tmp.renameTo(file);
        }
    }

    public static NBTCompound read(DataInputStream inputStream) throws IOException {
        return read(inputStream, NBTSizeTracker.INFINITE);
    }

    public static NBTCompound read(DataInput input, NBTSizeTracker sizeTracker) throws IOException {
        NBT<?> nbt = read(input, 0, sizeTracker);

        if (nbt instanceof NBTCompound c) {
            return c;
        } else {
            throw new IOException("Root tag must be a named compound tag");
        }
    }

    public static void write(NBTCompound compound, DataOutput output) throws IOException {
        writeTag(compound, output);
    }

    private static void writeTag(NBT<?> tag, DataOutput output) throws IOException {
        output.writeByte(tag.getTag().ordinal());

        if (tag.getTag() != Tag.END) {
            output.writeUTF("");
            tag.write(output);
        }
    }

    private static NBT<?> read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        Tag tag = Tag.values()[input.readByte()];
        sizeTracker.read(8); // Forge: Count everything!

        if (tag == Tag.END) {
            return new NBTEnd();
        } else {
            NBTSizeTracker.readUTF(sizeTracker, input.readUTF()); //Forge: Count this string.
            sizeTracker.read(32); //Forge: 4 extra bytes for the object allocation.
            return tag.read(input, depth, sizeTracker);
        }
    }

    public static void write(NBTCompound compound, File file) throws IOException {
        try (DataOutputStream stream = new DataOutputStream(new FileOutputStream(file))) {
            write(compound, stream);
        }
    }

    public static NBTCompound read(File file) throws IOException {
        if (!file.exists()) {
            return null;
        } else {
            try (DataInputStream stream = new DataInputStream(new FileInputStream(file))) {
                return read(stream, NBTSizeTracker.INFINITE);
            }
        }
    }
}