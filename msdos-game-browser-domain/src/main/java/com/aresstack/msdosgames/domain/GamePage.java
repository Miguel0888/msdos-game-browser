package com.aresstack.msdosgames.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class GamePage {

    private final List<GameSummary> games;
    private final String nextCursor;

    public GamePage(List<GameSummary> games, String nextCursor) {
        if (games == null) {
            throw new IllegalArgumentException("games must not be null");
        }
        this.games = new ArrayList<GameSummary>(games);
        this.nextCursor = normalizeCursor(nextCursor);
    }

    public List<GameSummary> getGames() {
        return Collections.unmodifiableList(games);
    }

    public String getNextCursor() {
        return nextCursor;
    }

    public boolean hasNextPage() {
        return nextCursor != null && nextCursor.length() > 0;
    }

    private static String normalizeCursor(String value) {
        if (value == null) {
            return null;
        }
        String trimmedValue = value.trim();
        if (trimmedValue.length() == 0) {
            return null;
        }
        return trimmedValue;
    }
}
