package net.minecraft.nbt;

import java.io.DataInput;
import java.io.IOException;
import java.util.function.Supplier;

public enum Tag {
    END(NBTEnd::new, NBTEnd::new),
    BYTE(NBTByte::new, () -> new NBTByte((byte) 0)),
    SHORT(NBTShort::new, () -> new NBTShort((short) 0)),
    INT(NBTInt::new, () -> new NBTInt(0)),
    LONG(NBTLong::new, () -> new NBTLong(0L)),
    FLOAT(NBTFloat::new, () -> new NBTFloat(0F)),
    DOUBLE(NBTDouble::new, () -> new NBTDouble(0.0)),
    BYTE_ARRAY(NBTByteArray::new, () -> new NBTByteArray(new byte[0])),
    STRING(NBTString::new, () -> new NBTString("")),
    LIST(NBTList::new, NBTList::new),
    COMPOUND(NBTCompound::new, NBTCompound::new),
    INT_ARRAY(NBTIntArray::new, () -> new NBTIntArray(new int[0])),
    LONG_ARRAY(NBTLongArray::new, () -> new NBTLongArray(new long[0]));

    private final NBT.Reader reader;
    private final Supplier<NBT> empty;

    Tag(NBT.Reader reader, Supplier<NBT> empty) {
        this.reader = reader;
        this.empty = empty;
    }

    public NBT read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        return this.reader.read(input, depth, sizeTracker);
    }

    public NBT empty() {
        return this.empty.get();
    }
}
