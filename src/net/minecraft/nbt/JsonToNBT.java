package net.minecraft.nbt;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class JsonToNBT {
    private static final Pattern DOUBLE_PATTERN_NOSUFFIX = Pattern.compile("[-+]?(?:[0-9]+[.]|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?", Pattern.CASE_INSENSITIVE);
    private static final Pattern DOUBLE_PATTERN = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?d", Pattern.CASE_INSENSITIVE);
    private static final Pattern FLOAT_PATTERN = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?f", Pattern.CASE_INSENSITIVE);
    private static final Pattern BYTE_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)b", Pattern.CASE_INSENSITIVE);
    private static final Pattern LONG_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)l", Pattern.CASE_INSENSITIVE);
    private static final Pattern SHORT_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)s", Pattern.CASE_INSENSITIVE);
    private static final Pattern INT_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)");
    private final String source;
    private int cursor;

    JsonToNBT(String source) {
        this.source = source;
    }

    public static NBTCompound getTagFromJson(String jsonString) throws JsonNBTException {
        return (new JsonToNBT(jsonString)).readSingleStruct();
    }

    NBTCompound readSingleStruct() throws JsonNBTException {
        NBTCompound result = readStruct();
        skipWhitespace();

        if (canRead()) {
            ++cursor;
            throw exception("Trailing data found");
        } else {
            return result;
        }
    }

    protected String readKey() throws JsonNBTException {
        skipWhitespace();

        if (!canRead()) {
            throw exception("Expected key");
        } else {
            return peek() == '"' ? readQuotedString() : readString();
        }
    }

    private JsonNBTException exception(String message) {
        return new JsonNBTException(message, source, cursor);
    }

    protected NBT readTypedValue() throws JsonNBTException {
        skipWhitespace();

        if (peek() == '"') {
            return new NBTString(readQuotedString());
        } else {
            String s = readString();

            if (s.isEmpty()) {
                throw exception("Expected value");
            } else {
                return type(s);
            }
        }
    }

    private NBT type(String stringIn) {
        try {
            if (FLOAT_PATTERN.matcher(stringIn).matches()) {
                return new NBTFloat(Float.parseFloat(stringIn.substring(0, stringIn.length() - 1)));
            }

            if (BYTE_PATTERN.matcher(stringIn).matches()) {
                return new NBTByte(Byte.parseByte(stringIn.substring(0, stringIn.length() - 1)));
            }

            if (LONG_PATTERN.matcher(stringIn).matches()) {
                return new NBTLong(Long.parseLong(stringIn.substring(0, stringIn.length() - 1)));
            }

            if (SHORT_PATTERN.matcher(stringIn).matches()) {
                return new NBTShort(Short.parseShort(stringIn.substring(0, stringIn.length() - 1)));
            }

            if (INT_PATTERN.matcher(stringIn).matches()) {
                return new NBTInt(Integer.parseInt(stringIn));
            }

            if (DOUBLE_PATTERN.matcher(stringIn).matches()) {
                return new NBTDouble(Double.parseDouble(stringIn.substring(0, stringIn.length() - 1)));
            }

            if (DOUBLE_PATTERN_NOSUFFIX.matcher(stringIn).matches()) {
                return new NBTDouble(Double.parseDouble(stringIn));
            }

            if ("true".equalsIgnoreCase(stringIn)) {
                return new NBTByte((byte) 1);
            }

            if ("false".equalsIgnoreCase(stringIn)) {
                return new NBTByte((byte) 0);
            }
        } catch (NumberFormatException ignored) {
        }

        return new NBTString(stringIn);
    }

    private String readQuotedString() throws JsonNBTException {
        int i = ++cursor;
        StringBuilder stringbuilder = null;
        boolean flag = false;

        while (canRead()) {
            char symbol = pop();

            if (flag) {
                if (symbol != '\\' && symbol != '"') {
                    throw exception("Invalid escape of '" + symbol + "'");
                }

                flag = false;
            } else {
                if (symbol == '\\') {
                    flag = true;

                    if (stringbuilder == null) {
                        stringbuilder = new StringBuilder(source.substring(i, cursor - 1));
                    }

                    continue;
                }

                if (symbol == '"') {
                    return stringbuilder == null ? source.substring(i, cursor - 1) : stringbuilder.toString();
                }
            }

            if (stringbuilder != null) {
                stringbuilder.append(symbol);
            }
        }

        throw exception("Missing termination quote");
    }

    private String readString() {
        int i = cursor;

        while (canRead() && isAllowedInKey(peek())) {
            ++cursor;
        }

        return source.substring(i, cursor);
    }

    protected NBT readValue() throws JsonNBTException {
        skipWhitespace();

        if (!canRead()) {
            throw exception("Expected value");
        } else {
            char symbol = peek();

            if (symbol == '{') {
                return readStruct();
            } else {
                return symbol == '[' ? readList() : readTypedValue();
            }
        }
    }

    protected NBT readList() throws JsonNBTException {
        return canRead(2) && peek(1) != '"' && peek(2) == ';' ? readArrayTag() : readListTag();
    }

    protected NBTCompound readStruct() throws JsonNBTException {
        expect('{');
        NBTCompound result = new NBTCompound();
        skipWhitespace();

        while (canRead() && peek() != '}') {
            String s = readKey();

            if (s.isEmpty()) {
                throw exception("Expected non-empty key");
            }

            expect(':');
            result.setTag(s, readValue());

            if (!hasElementSeparator()) {
                break;
            }

            if (!canRead()) {
                throw exception("Expected key");
            }
        }

        expect('}');
        return result;
    }

    private NBT readListTag() throws JsonNBTException {
        expect('[');
        skipWhitespace();

        if (!canRead()) {
            throw exception("Expected value");
        } else {
            NBTList result = new NBTList();
            Tag i = null;

            while (peek() != ']') {
                NBT nbt = readValue();
                Tag tag = nbt.getTag();

                if (i == null) {
                    i = tag;
                } else if (tag != i) {
                    throw exception("Unable to insert " + tag.name() + " into ListTag of type " + i.name());
                }

                result.push(nbt);

                if (!hasElementSeparator()) {
                    break;
                }

                if (!canRead()) {
                    throw exception("Expected value");
                }
            }

            expect(']');
            return result;
        }
    }

    private NBT readArrayTag() throws JsonNBTException {
        expect('[');
        char symbol = pop();
        pop();
        skipWhitespace();

        if (!canRead()) {
            throw exception("Expected value");
        } else if (symbol == 'B') {
            return new NBTByteArray(readArray(Tag.BYTE_ARRAY, Tag.BYTE));
        } else if (symbol == 'L') {
            return new NBTLongArray(readArray(Tag.LONG_ARRAY, Tag.LONG));
        } else if (symbol == 'I') {
            return new NBTIntArray(readArray(Tag.INT_ARRAY, Tag.INT));
        } else {
            throw exception("Invalid array type '" + symbol + "' found");
        }
    }

    private <T extends Number> List<T> readArray(Tag arrayTag, Tag tag) throws JsonNBTException {
        List<T> result = new ArrayList<>();

        while (true) {
            if (peek() != ']') {
                NBT nbt = readValue();
                Tag i = nbt.getTag();

                if (i != tag) {
                    throw exception("Unable to insert " + i.name() + " into " + arrayTag.name());
                }

                if (tag == Tag.BYTE) {
                    result.add((T) Byte.valueOf(((NBTPrimitive) nbt).getByte()));
                } else if (tag == Tag.LONG) {
                    result.add((T) Long.valueOf(((NBTPrimitive) nbt).getLong()));
                } else {
                    result.add((T) Integer.valueOf(((NBTPrimitive) nbt).getInt()));
                }

                if (hasElementSeparator()) {
                    if (!canRead()) {
                        throw exception("Expected value");
                    }

                    continue;
                }
            }

            expect(']');
            return result;
        }
    }

    private void skipWhitespace() {
        while (canRead() && Character.isWhitespace(peek())) {
            ++cursor;
        }
    }

    private boolean hasElementSeparator() {
        skipWhitespace();

        if (canRead() && peek() == ',') {
            ++cursor;
            skipWhitespace();
            return true;
        } else {
            return false;
        }
    }

    private void expect(char expected) throws JsonNBTException {
        skipWhitespace();
        boolean canRead = canRead();

        if (canRead && peek() == expected) {
            ++cursor;
        } else {
            throw new JsonNBTException("Expected '" + expected + "' but got '" + (canRead ? peek() : "<EOF>") + "'", source, cursor + 1);
        }
    }

    protected boolean isAllowedInKey(char symbol) {
        return symbol >= '0' && symbol <= '9' || symbol >= 'A' && symbol <= 'Z' || symbol >= 'a' && symbol <= 'z' || symbol == '_' || symbol == '-' || symbol == '.' || symbol == '+';
    }

    private boolean canRead(int i) {
        return cursor + i < source.length();
    }

    boolean canRead() {
        return canRead(0);
    }

    private char peek(int i) {
        return source.charAt(cursor + i);
    }

    private char peek() {
        return peek(0);
    }

    private char pop() {
        return source.charAt(cursor++);
    }
}