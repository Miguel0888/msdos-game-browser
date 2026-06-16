package de.bund.zrb.msdosgames.domain;

public final class GameFile {

    private final String name;
    private final String format;
    private final long size;
    private final String md5;
    private final String sha1;

    public GameFile(String name, String format, long size, String md5, String sha1) {
        this.name = requireText(name, "name");
        this.format = textOrEmpty(format);
        this.size = Math.max(0L, size);
        this.md5 = textOrEmpty(md5);
        this.sha1 = textOrEmpty(sha1);
    }

    public String getName() {
        return name;
    }

    public String getFormat() {
        return format;
    }

    public long getSize() {
        return size;
    }

    public String getMd5() {
        return md5;
    }

    public String getSha1() {
        return sha1;
    }

    private static String requireText(String value, String fieldName) {
        String cleanedValue = textOrEmpty(value);
        if (cleanedValue.length() == 0) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return cleanedValue;
    }

    private static String textOrEmpty(String value) {
        if (value == null) {
            return "";
        }
        return value.trim();
    }

    @Override
    public String toString() {
        if (format.length() == 0) {
            return name;
        }
        return name + " (" + format + ")";
    }
}
