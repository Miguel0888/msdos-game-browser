package com.aresstack.msdosgames.application.port;

import com.aresstack.msdosgames.domain.FavoriteGame;
import com.aresstack.msdosgames.domain.GameIdentifier;

import java.io.IOException;
import java.util.List;

public interface FavoriteGameStore {

    List<FavoriteGame> listFavorites() throws IOException;

    boolean isFavorite(GameIdentifier identifier) throws IOException;

    void addFavorite(FavoriteGame favoriteGame) throws IOException;

    void removeFavorite(GameIdentifier identifier) throws IOException;
}
