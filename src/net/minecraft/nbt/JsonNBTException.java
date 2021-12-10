package net.minecraft.nbt;

public class JsonNBTException extends Exception {
    public JsonNBTException(String message, String json, int cursor) {
        super(message + " at: " + slice(json, cursor));
    }

    private static String slice(String json, int cursor) {
        StringBuilder result = new StringBuilder();
        int i = Math.min(json.length(), cursor);

        if (i > 35) {
            result.append("...");
        }

        result.append(json, Math.max(0, i - 35), i);
        result.append("<--[HERE]");
        return result.toString();
    }
}