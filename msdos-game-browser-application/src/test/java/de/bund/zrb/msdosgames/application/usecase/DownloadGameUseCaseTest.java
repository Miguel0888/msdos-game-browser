package de.bund.zrb.msdosgames.application.usecase;

import de.bund.zrb.msdosgames.application.port.DownloadProgressListener;
import de.bund.zrb.msdosgames.application.port.GameDownloader;
import de.bund.zrb.msdosgames.application.port.ArchiveNoticeAcceptanceStore;
import de.bund.zrb.msdosgames.domain.ArchiveItemNotice;
import de.bund.zrb.msdosgames.domain.DownloadRequest;
import de.bund.zrb.msdosgames.domain.GameFile;
import de.bund.zrb.msdosgames.domain.GameIdentifier;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DownloadGameUseCaseTest {

    @Test
    void blocksDownloadWhenArchiveNoticeWasNotAccepted() throws Exception {
        RecordingDownloader downloader = new RecordingDownloader();
        DownloadGameUseCase useCase = new DownloadGameUseCase(new RejectingLicenseStore(), downloader);

        assertThrows(ArchiveNoticeNotAcceptedException.class, new org.junit.jupiter.api.function.Executable() {
            @Override
            public void execute() throws Throwable {
                useCase.downloadAcceptedGame(
                        GameIdentifier.of("doom"),
                        new ArchiveItemNotice("Download Options", "access", "source", "", false, true, Collections.emptyList()),
                        new GameFile("doom.zip", "ZIP", 42L, "", ""),
                        new File("build/test-downloads"),
                        null);
            }
        });

        assertFalse(downloader.wasCalled());
    }

    private static final class RejectingLicenseStore implements ArchiveNoticeAcceptanceStore {
        @Override
        public boolean hasAccepted(GameIdentifier identifier, ArchiveItemNotice archiveItemNotice) {
            return false;
        }

        @Override
        public void accept(GameIdentifier identifier, ArchiveItemNotice archiveItemNotice) {
        }
    }

    private static final class RecordingDownloader implements GameDownloader {
        private boolean called;

        @Override
        public void download(DownloadRequest request, DownloadProgressListener progressListener) throws IOException {
            called = true;
        }

        boolean wasCalled() {
            return called;
        }
    }
}
