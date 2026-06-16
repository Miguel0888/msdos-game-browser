package de.bund.zrb.msdosgames.application.usecase;

import de.bund.zrb.msdosgames.application.port.GameCatalog;
import de.bund.zrb.msdosgames.domain.GamePage;
import de.bund.zrb.msdosgames.domain.GameSearchCriteria;

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
