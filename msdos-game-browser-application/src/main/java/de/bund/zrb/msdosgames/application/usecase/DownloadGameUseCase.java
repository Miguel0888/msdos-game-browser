package de.bund.zrb.msdosgames.application.usecase;

import de.bund.zrb.msdosgames.application.port.DownloadProgressListener;
import de.bund.zrb.msdosgames.application.port.GameDownloader;
import de.bund.zrb.msdosgames.application.port.ArchiveNoticeAcceptanceStore;
import de.bund.zrb.msdosgames.domain.DownloadRequest;
import de.bund.zrb.msdosgames.domain.GameFile;
import de.bund.zrb.msdosgames.domain.GameIdentifier;
import de.bund.zrb.msdosgames.domain.ArchiveItemNotice;

import java.io.File;
import java.io.IOException;

public final class DownloadGameUseCase {

    private final ArchiveNoticeAcceptanceStore acceptanceStore;
    private final GameDownloader gameDownloader;

    public DownloadGameUseCase(
            ArchiveNoticeAcceptanceStore acceptanceStore,
            GameDownloader gameDownloader) {
        if (acceptanceStore == null) {
            throw new IllegalArgumentException("acceptanceStore must not be null");
        }
        if (gameDownloader == null) {
            throw new IllegalArgumentException("gameDownloader must not be null");
        }
        this.acceptanceStore = acceptanceStore;
        this.gameDownloader = gameDownloader;
    }

    public void downloadAcceptedGame(
            GameIdentifier identifier,
            ArchiveItemNotice archiveItemNotice,
            GameFile gameFile,
            File targetDirectory,
            DownloadProgressListener progressListener) throws IOException {
        if (!acceptanceStore.hasAccepted(identifier, archiveItemNotice)) {
            throw new ArchiveNoticeNotAcceptedException(identifier);
        }

        gameDownloader.download(new DownloadRequest(identifier, gameFile, targetDirectory), progressListener);
    }
}
