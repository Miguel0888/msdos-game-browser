package com.aresstack.msdosgames.backend;

import com.aresstack.msdosgames.application.port.GameBrowserBackendService;
import com.aresstack.msdosgames.application.usecase.AcceptArchiveNoticeUseCase;
import com.aresstack.msdosgames.application.usecase.DownloadGameUseCase;
import com.aresstack.msdosgames.application.usecase.FavoriteGamesUseCase;
import com.aresstack.msdosgames.infrastructure.archive.InternetArchiveCatalogClient;
import com.aresstack.msdosgames.infrastructure.archive.InternetArchiveDownloadClient;
import com.aresstack.msdosgames.infrastructure.archive.JdkHttpGateway;
import com.aresstack.msdosgames.infrastructure.local.ApplicationDirectories;
import com.aresstack.msdosgames.infrastructure.local.FileBasedFavoriteGameStore;

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
