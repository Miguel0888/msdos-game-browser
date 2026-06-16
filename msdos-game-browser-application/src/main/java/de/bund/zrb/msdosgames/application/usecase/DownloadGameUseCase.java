package de.bund.zrb.msdosgames.application.usecase;

import de.bund.zrb.msdosgames.application.port.DownloadProgressListener;
import de.bund.zrb.msdosgames.application.port.GameDownloader;
import de.bund.zrb.msdosgames.application.port.LicenseAcceptanceStore;
import de.bund.zrb.msdosgames.domain.DownloadRequest;
import de.bund.zrb.msdosgames.domain.GameFile;
import de.bund.zrb.msdosgames.domain.GameIdentifier;
import de.bund.zrb.msdosgames.domain.LicenseNotice;

import java.io.File;
import java.io.IOException;

public final class DownloadGameUseCase {

    private final LicenseAcceptanceStore licenseAcceptanceStore;
    private final GameDownloader gameDownloader;

    public DownloadGameUseCase(
            LicenseAcceptanceStore licenseAcceptanceStore,
            GameDownloader gameDownloader) {
        if (licenseAcceptanceStore == null) {
            throw new IllegalArgumentException("licenseAcceptanceStore must not be null");
        }
        if (gameDownloader == null) {
            throw new IllegalArgumentException("gameDownloader must not be null");
        }
        this.licenseAcceptanceStore = licenseAcceptanceStore;
        this.gameDownloader = gameDownloader;
    }

    public void downloadAcceptedGame(
            GameIdentifier identifier,
            LicenseNotice licenseNotice,
            GameFile gameFile,
            File targetDirectory,
            DownloadProgressListener progressListener) throws IOException {
        if (!licenseAcceptanceStore.hasAccepted(identifier, licenseNotice)) {
            throw new LicenseNotAcceptedException(identifier);
        }

        gameDownloader.download(new DownloadRequest(identifier, gameFile, targetDirectory), progressListener);
    }
}
