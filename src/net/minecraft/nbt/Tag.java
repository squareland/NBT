package net.minecraft.nbt;

import java.io.DataInput;
import java.io.IOException;

public enum Tag {
    END(NBTEnd::new),
    BYTE(NBTByte::new),
    SHORT(NBTShort::new),
    INT(NBTInt::new),
    LONG(NBTLong::new),
    FLOAT(NBTFloat::new),
    DOUBLE(NBTDouble::new),
    BYTE_ARRAY(NBTByteArray::new),
    STRING(NBTString::new),
    LIST(NBTList::new),
    COMPOUND(NBTCompound::new),
    INT_ARRAY(NBTIntArray::new),
    LONG_ARRAY(NBTLongArray::new);

    private final NBT.Reader reader;

    Tag(NBT.Reader reader) {
        this.reader = reader;
    }

    public NBT read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        return this.reader.read(input, depth, sizeTracker);
    }
}
