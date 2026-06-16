package com.aresstack.msdosgames.application.usecase;

import com.aresstack.msdosgames.application.port.GameCatalog;
import com.aresstack.msdosgames.domain.GamePage;
import com.aresstack.msdosgames.domain.GameSearchCriteria;

import java.io.IOException;

public final class SearchGamesUseCase {

    private final GameCatalog gameCatalog;

    public SearchGamesUseCase(GameCatalog gameCatalog) {
        if (gameCatalog == null) {
            throw new IllegalArgumentException("gameCatalog must not be null");
        }
        this.gameCatalog = gameCatalog;
    }

    public GamePage search(String query, int pageSize) throws IOException {
        return gameCatalog.search(GameSearchCriteria.searchFirstPage(query, pageSize));
    }

    public GamePage searchNextPage(String query, String cursor, int pageSize) throws IOException {
        return gameCatalog.search(GameSearchCriteria.searchNextPage(query, cursor, pageSize));
    }
}
