package com.aresstack.msdosgames.application.usecase;

import com.aresstack.msdosgames.application.port.GameDetailsProvider;
import com.aresstack.msdosgames.domain.GameDetails;
import com.aresstack.msdosgames.domain.GameIdentifier;

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
