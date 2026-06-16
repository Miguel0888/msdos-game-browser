package com.aresstack.msdosgames.domain;

public final class FavoriteGame {

    private final GameIdentifier identifier;
    private final String title;
    private final String description;
    private final String creator;
    private final String date;
    private final String sourceUrl;
    private final long addedAtMillis;

    public FavoriteGame(
            GameIdentifier identifier,
            String title,
            String description,
            String creator,
            String date,
            String sourceUrl,
            long addedAtMillis) {
        if (identifier == null) {
            throw new IllegalArgumentException("identifier must not be null");
        }
        this.identifier = identifier;
        this.title = textOrFallback(title, identifier.getValue());
        this.description = textOrEmpty(description);
        this.creator = textOrEmpty(creator);
        this.date = textOrEmpty(date);
        this.sourceUrl = textOrEmpty(sourceUrl);
        this.addedAtMillis = Math.max(0L, addedAtMillis);
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

    public String getSourceUrl() {
        return sourceUrl;
    }

    public long getAddedAtMillis() {
        return addedAtMillis;
    }

    public GameSummary toGameSummary() {
        return new GameSummary(identifier, title, description, creator, date, "", 0L, 0L);
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
