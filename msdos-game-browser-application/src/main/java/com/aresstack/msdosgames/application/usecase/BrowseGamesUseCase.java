package com.aresstack.msdosgames.application.usecase;

import com.aresstack.msdosgames.application.port.GameCatalog;
import com.aresstack.msdosgames.domain.GamePage;
import com.aresstack.msdosgames.domain.GameSearchCriteria;

import java.io.IOException;

public final class BrowseGamesUseCase {

    private final GameCatalog gameCatalog;

    public BrowseGamesUseCase(GameCatalog gameCatalog) {
        if (gameCatalog == null) {
            throw new IllegalArgumentException("gameCatalog must not be null");
        }
        this.gameCatalog = gameCatalog;
    }

    public GamePage browseFirstPage(int pageSize) throws IOException {
        return gameCatalog.browse(GameSearchCriteria.browseFirstPage(pageSize));
    }

    public GamePage browseNextPage(String cursor, int pageSize) throws IOException {
        return gameCatalog.browse(GameSearchCriteria.browseNextPage(cursor, pageSize));
    }
}
