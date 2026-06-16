package de.bund.zrb.msdosgames.application.usecase;

import de.bund.zrb.msdosgames.application.port.GameCatalog;
import de.bund.zrb.msdosgames.domain.GamePage;
import de.bund.zrb.msdosgames.domain.GameSearchCriteria;

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
