package com.aresstack.msdosgames.application.usecase;

import com.aresstack.msdosgames.application.port.DownloadProgressListener;
import com.aresstack.msdosgames.application.port.GameDownloader;
import com.aresstack.msdosgames.application.port.ArchiveNoticeAcceptanceStore;
import com.aresstack.msdosgames.domain.DownloadRequest;
import com.aresstack.msdosgames.domain.GameFile;
import com.aresstack.msdosgames.domain.GameIdentifier;
import com.aresstack.msdosgames.domain.ArchiveItemNotice;

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
