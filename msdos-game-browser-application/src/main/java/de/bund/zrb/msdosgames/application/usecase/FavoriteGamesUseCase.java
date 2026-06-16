package de.bund.zrb.msdosgames.application.usecase;

import de.bund.zrb.msdosgames.application.port.FavoriteGameStore;
import de.bund.zrb.msdosgames.domain.FavoriteGame;
import de.bund.zrb.msdosgames.domain.GameDetails;
import de.bund.zrb.msdosgames.domain.GameIdentifier;
import de.bund.zrb.msdosgames.domain.GameSummary;

import java.io.IOException;
import java.util.List;

public final class FavoriteGamesUseCase {

    private final FavoriteGameStore favoriteGameStore;

    public FavoriteGamesUseCase(FavoriteGameStore favoriteGameStore) {
        if (favoriteGameStore == null) {
            throw new IllegalArgumentException("favoriteGameStore must not be null");
        }
        this.favoriteGameStore = favoriteGameStore;
    }

    public List<FavoriteGame> listFavorites() throws IOException {
        return favoriteGameStore.listFavorites();
    }

    public boolean isFavorite(GameIdentifier identifier) throws IOException {
        return favoriteGameStore.isFavorite(identifier);
    }

    public void addFavorite(GameSummary summary, GameDetails details) throws IOException {
        if (details == null) {
            throw new IllegalArgumentException("details must not be null");
        }
        FavoriteGame favoriteGame = new FavoriteGame(
                details.getIdentifier(),
                selectTitle(summary, details),
                selectDescription(summary, details),
                summary == null ? "" : summary.getCreator(),
                summary == null ? "" : summary.getDate(),
                details.getArchiveItemNotice().getSourceUrl(),
                System.currentTimeMillis());
        favoriteGameStore.addFavorite(favoriteGame);
    }

    public void removeFavorite(GameIdentifier identifier) throws IOException {
        favoriteGameStore.removeFavorite(identifier);
    }

    private String selectTitle(GameSummary summary, GameDetails details) {
        if (summary != null && summary.getTitle().length() > 0) {
            return summary.getTitle();
        }
        return details.getTitle();
    }

    private String selectDescription(GameSummary summary, GameDetails details) {
        if (summary != null && summary.getDescription().length() > 0) {
            return summary.getDescription();
        }
        return details.getDescriptionText();
    }
}
