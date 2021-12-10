package net.minecraft.nbt;

public class NBTSizeTracker {
    public static final NBTSizeTracker INFINITE = new NBTSizeTracker(0L) {
        @Override
        public void read(long bits) {
        }
    };
    private final long max;
    private long read;

    public NBTSizeTracker(long max) {
        this.max = max;
    }

    public static void readUTF(NBTSizeTracker tracker, String data) {
        tracker.read(16); //Header length
        if (data == null) {
            return;
        }

        int len = data.length();
        int utflen = 0;

        for (int i = 0; i < len; i++) {
            int c = data.charAt(i);
            if ((c >= 0x0001) && (c <= 0x007F)) {
                utflen += 1;
            } else if (c > 0x07FF) {
                utflen += 3;
            } else {
                utflen += 2;
            }
        }
        tracker.read(8 * utflen);
    }

    public void read(long bits) {
        this.read += bits / 8L;

        if (this.read > this.max) {
            throw new RuntimeException("Tried to read NBT tag that was too big; tried to allocate: " + this.read + "bytes where max allowed: " + this.max);
        }
    }
}