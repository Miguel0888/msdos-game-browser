package de.bund.zrb.msdosgames.domain;

public final class GameIdentifier {

    private final String value;

    private GameIdentifier(String value) {
        this.value = value;
    }

    public static GameIdentifier of(String value) {
        String normalizedValue = requireText(value, "identifier");
        return new GameIdentifier(normalizedValue);
    }

    public String getValue() {
        return value;
    }

    private static String requireText(String value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " must not be null");
        }

        String trimmedValue = value.trim();
        if (trimmedValue.length() == 0) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }

        return trimmedValue;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof GameIdentifier)) {
            return false;
        }

        GameIdentifier that = (GameIdentifier) other;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}
