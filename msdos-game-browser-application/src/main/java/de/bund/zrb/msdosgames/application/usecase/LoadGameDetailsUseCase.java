package de.bund.zrb.msdosgames.application.usecase;

import de.bund.zrb.msdosgames.application.port.GameDetailsProvider;
import de.bund.zrb.msdosgames.domain.GameDetails;
import de.bund.zrb.msdosgames.domain.GameIdentifier;

import java.io.IOException;

public final class LoadGameDetailsUseCase {

    private final GameDetailsProvider gameDetailsProvider;

    public LoadGameDetailsUseCase(GameDetailsProvider gameDetailsProvider) {
        if (gameDetailsProvider == null) {
            throw new IllegalArgumentException("gameDetailsProvider must not be null");
        }
        this.gameDetailsProvider = gameDetailsProvider;
    }

    public GameDetails loadDetails(GameIdentifier identifier) throws IOException {
        return gameDetailsProvider.loadDetails(identifier);
    }
}
