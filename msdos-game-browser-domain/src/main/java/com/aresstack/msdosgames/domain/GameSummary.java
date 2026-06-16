package com.aresstack.msdosgames.domain;

public final class GameSummary {

    private final GameIdentifier identifier;
    private final String title;
    private final String description;
    private final String creator;
    private final String date;
    private final String publicDate;
    private final long downloads;
    private final long itemSize;

    public GameSummary(
            GameIdentifier identifier,
            String title,
            String description,
            String creator,
            String date,
            String publicDate,
            long downloads,
            long itemSize) {
        if (identifier == null) {
            throw new IllegalArgumentException("identifier must not be null");
        }
        this.identifier = identifier;
        this.title = textOrFallback(title, identifier.getValue());
        this.description = textOrEmpty(description);
        this.creator = textOrEmpty(creator);
        this.date = textOrEmpty(date);
        this.publicDate = textOrEmpty(publicDate);
        this.downloads = Math.max(0L, downloads);
        this.itemSize = Math.max(0L, itemSize);
    }

    public GameIdentifier getIdentifier() {
        return identifier;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCreator() {
        return creator;
    }

    public String getDate() {
        return date;
    }

    public String getPublicDate() {
        return publicDate;
    }

    public long getDownloads() {
        return downloads;
    }

    public long getItemSize() {
        return itemSize;
    }

    private static String textOrFallback(String value, String fallback) {
        String cleanedValue = textOrEmpty(value);
        if (cleanedValue.length() == 0) {
            return fallback;
        }
        return cleanedValue;
    }

    private static String textOrEmpty(String value) {
        if (value == null) {
            return "";
        }
        return value.trim();
    }
}
