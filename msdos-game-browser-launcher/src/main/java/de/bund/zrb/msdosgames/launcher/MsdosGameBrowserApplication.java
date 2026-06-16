package de.bund.zrb.msdosgames.launcher;

import de.bund.zrb.msdosgames.application.usecase.AcceptArchiveNoticeUseCase;
import de.bund.zrb.msdosgames.application.usecase.BrowseGamesUseCase;
import de.bund.zrb.msdosgames.application.usecase.DownloadGameUseCase;
import de.bund.zrb.msdosgames.application.usecase.FavoriteGamesUseCase;
import de.bund.zrb.msdosgames.backend.H2ArchiveNoticeAcceptanceStore;
import de.bund.zrb.msdosgames.backend.LuceneH2GameBrowserBackendService;
import de.bund.zrb.msdosgames.frontend.swing.GameBrowserFrame;
import de.bund.zrb.msdosgames.infrastructure.archive.InternetArchiveCatalogClient;
import de.bund.zrb.msdosgames.infrastructure.archive.InternetArchiveDownloadClient;
import de.bund.zrb.msdosgames.infrastructure.archive.JdkHttpGateway;
import de.bund.zrb.msdosgames.infrastructure.local.ApplicationDirectories;
import de.bund.zrb.msdosgames.infrastructure.local.FileBasedFavoriteGameStore;

import javax.swing.SwingUtilities;

public final class MsdosGameBrowserApplication {

    private MsdosGameBrowserApplication() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createFrame().showBrowser();
            }
        });
    }

    private static GameBrowserFrame createFrame() {
        JdkHttpGateway httpGateway = new JdkHttpGateway();
        InternetArchiveCatalogClient archiveCatalogClient = new InternetArchiveCatalogClient(httpGateway);
        InternetArchiveDownloadClient downloadClient = new InternetArchiveDownloadClient();
        H2ArchiveNoticeAcceptanceStore archiveNoticeAcceptanceStore = new H2ArchiveNoticeAcceptanceStore(ApplicationDirectories.defaultDatabaseDirectory());
        FileBasedFavoriteGameStore favoriteGameStore = new FileBasedFavoriteGameStore(ApplicationDirectories.defaultFavoritesFile());
        LuceneH2GameBrowserBackendService backendService = new LuceneH2GameBrowserBackendService(
                archiveCatalogClient,
                archiveCatalogClient,
                ApplicationDirectories.defaultDatabaseDirectory());

        return new GameBrowserFrame(
                backendService,
                new AcceptArchiveNoticeUseCase(archiveNoticeAcceptanceStore),
                new DownloadGameUseCase(archiveNoticeAcceptanceStore, downloadClient),
                new FavoriteGamesUseCase(favoriteGameStore),
                ApplicationDirectories.defaultApplicationDirectory(),
                ApplicationDirectories.defaultDownloadDirectory());
    }
}
