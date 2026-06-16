package de.bund.zrb.msdosgames.application.port;

import de.bund.zrb.msdosgames.domain.FavoriteGame;
import de.bund.zrb.msdosgames.domain.GameIdentifier;

import java.io.IOException;
import java.util.List;

public interface FavoriteGameStore {

    List<FavoriteGame> listFavorites() throws IOException;

    boolean isFavorite(GameIdentifier identifier) throws IOException;

    void addFavorite(FavoriteGame favoriteGame) throws IOException;

    void removeFavorite(GameIdentifier identifier) throws IOException;
}
