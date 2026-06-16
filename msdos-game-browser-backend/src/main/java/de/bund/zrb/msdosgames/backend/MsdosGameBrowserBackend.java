package de.bund.zrb.msdosgames.backend;

import de.bund.zrb.msdosgames.application.port.GameBrowserBackendService;
import de.bund.zrb.msdosgames.application.usecase.AcceptArchiveNoticeUseCase;
import de.bund.zrb.msdosgames.application.usecase.DownloadGameUseCase;
import de.bund.zrb.msdosgames.application.usecase.FavoriteGamesUseCase;
import de.bund.zrb.msdosgames.infrastructure.archive.InternetArchiveCatalogClient;
import de.bund.zrb.msdosgames.infrastructure.archive.InternetArchiveDownloadClient;
import de.bund.zrb.msdosgames.infrastructure.archive.JdkHttpGateway;
import de.bund.zrb.msdosgames.infrastructure.local.ApplicationDirectories;
import de.bund.zrb.msdosgames.infrastructure.local.FileBasedFavoriteGameStore;

import java.io.File;

public final class MsdosGameBrowserBackend {

    private final GameBrowserBackendService browserService;
    private final AcceptArchiveNoticeUseCase acceptArchiveNoticeUseCase;
    private final DownloadGameUseCase downloadGameUseCase;
    private final FavoriteGamesUseCase favoriteGamesUseCase;
    private final File applicationDirectory;
    private final File downloadDirectory;
    private final H2ArchiveNoticeAcceptanceStore archiveNoticeAcceptanceStore;

    private MsdosGameBrowserBackend(
            GameBrowserBackendService browserService,
            AcceptArchiveNoticeUseCase acceptArchiveNoticeUseCase,
            DownloadGameUseCase downloadGameUseCase,
            FavoriteGamesUseCase favoriteGamesUseCase,
            File applicationDirectory,
            File downloadDirectory,
            H2ArchiveNoticeAcceptanceStore archiveNoticeAcceptanceStore) {
        this.browserService = browserService;
        this.acceptArchiveNoticeUseCase = acceptArchiveNoticeUseCase;
        this.downloadGameUseCase = downloadGameUseCase;
        this.favoriteGamesUseCase = favoriteGamesUseCase;
        this.applicationDirectory = applicationDirectory;
        this.downloadDirectory = downloadDirectory;
        this.archiveNoticeAcceptanceStore = archiveNoticeAcceptanceStore;
    }

    public static MsdosGameBrowserBackend createDefault() {
        return create(PreloadConfiguration.disabled());
    }

    public static MsdosGameBrowserBackend create(PreloadConfiguration preloadConfiguration) {
        JdkHttpGateway httpGateway = new JdkHttpGateway();
        InternetArchiveCatalogClient archiveCatalogClient = new InternetArchiveCatalogClient(httpGateway);
        InternetArchiveDownloadClient downloadClient = new InternetArchiveDownloadClient();
        H2ArchiveNoticeAcceptanceStore archiveNoticeAcceptanceStore = new H2ArchiveNoticeAcceptanceStore(ApplicationDirectories.defaultDatabaseDirectory());
        FileBasedFavoriteGameStore favoriteGameStore = new FileBasedFavoriteGameStore(ApplicationDirectories.defaultFavoritesFile());
        LuceneH2GameBrowserBackendService browserService = new LuceneH2GameBrowserBackendService(
                archiveCatalogClient,
                archiveCatalogClient,
                ApplicationDirectories.defaultDatabaseDirectory(),
                preloadConfiguration);

        return new MsdosGameBrowserBackend(
                browserService,
                new AcceptArchiveNoticeUseCase(archiveNoticeAcceptanceStore),
                new DownloadGameUseCase(archiveNoticeAcceptanceStore, downloadClient),
                new FavoriteGamesUseCase(favoriteGameStore),
                ApplicationDirectories.defaultApplicationDirectory(),
                ApplicationDirectories.defaultDownloadDirectory(),
                archiveNoticeAcceptanceStore);
    }

    public GameBrowserBackendService getBrowserService() {
        return browserService;
    }

    public AcceptArchiveNoticeUseCase getAcceptArchiveNoticeUseCase() {
        return acceptArchiveNoticeUseCase;
    }

    public DownloadGameUseCase getDownloadGameUseCase() {
        return downloadGameUseCase;
    }

    public FavoriteGamesUseCase getFavoriteGamesUseCase() {
        return favoriteGamesUseCase;
    }

    public File getApplicationDirectory() {
        return applicationDirectory;
    }

    public File getDownloadDirectory() {
        return downloadDirectory;
    }

    public void shutdown() {
        browserService.shutdown();
        archiveNoticeAcceptanceStore.close();
    }
}
