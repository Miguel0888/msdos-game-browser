package com.aresstack.msdosgames.domain;

public final class ArchiveMetadataEntry {

    private final String name;
    private final String value;

    public ArchiveMetadataEntry(String name, String value) {
        this.name = textOrEmpty(name);
        this.value = textOrEmpty(value);
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    private static String textOrEmpty(String value) {
        if (value == null) {
            return "";
        }
        return value.trim();
    }
}
