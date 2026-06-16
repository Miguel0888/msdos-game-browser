package de.bund.zrb.msdosgames.frontend.swing;

import de.bund.zrb.msdosgames.application.usecase.LoadGameDetailsUseCase;
import de.bund.zrb.msdosgames.domain.GameDetails;
import de.bund.zrb.msdosgames.domain.GameIdentifier;
import de.bund.zrb.msdosgames.domain.GameSummary;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

final class GameDetailsPreloader {

    private final LoadGameDetailsUseCase loadGameDetailsUseCase;
    private final ExecutorService executorService;
    private final Map<GameIdentifier, Future<GameDetails>> detailsByIdentifier = new ConcurrentHashMap<GameIdentifier, Future<GameDetails>>();

    GameDetailsPreloader(LoadGameDetailsUseCase loadGameDetailsUseCase) {
        if (loadGameDetailsUseCase == null) {
            throw new IllegalArgumentException("loadGameDetailsUseCase must not be null");
        }
        this.loadGameDetailsUseCase = loadGameDetailsUseCase;
        this.executorService = Executors.newFixedThreadPool(3);
    }

    void preload(List<GameSummary> games, int firstIndex, int lastIndex) {
        if (games == null || games.isEmpty()) {
            return;
        }

        int safeFirstIndex = Math.max(0, firstIndex);
        int safeLastIndex = Math.min(games.size() - 1, lastIndex);
        for (int index = safeFirstIndex; index <= safeLastIndex; index++) {
            preload(games.get(index).getIdentifier());
        }
    }

    Future<GameDetails> preload(GameIdentifier identifier) {
        Future<GameDetails> existingFuture = detailsByIdentifier.get(identifier);
        if (existingFuture != null) {
            return existingFuture;
        }

        Future<GameDetails> newFuture = executorService.submit(new LoadDetailsTask(identifier));
        Future<GameDetails> racedFuture = detailsByIdentifier.putIfAbsent(identifier, newFuture);
        if (racedFuture != null) {
            newFuture.cancel(true);
            return racedFuture;
        }
        return newFuture;
    }

    GameDetails load(GameIdentifier identifier) throws Exception {
        return preload(identifier).get();
    }

    void shutdown() {
        executorService.shutdownNow();
    }

    private final class LoadDetailsTask implements Callable<GameDetails> {

        private final GameIdentifier identifier;

        private LoadDetailsTask(GameIdentifier identifier) {
            this.identifier = identifier;
        }

        @Override
        public GameDetails call() throws Exception {
            return loadGameDetailsUseCase.loadDetails(identifier);
        }
    }
}
