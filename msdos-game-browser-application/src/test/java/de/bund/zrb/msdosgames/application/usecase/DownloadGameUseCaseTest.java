package de.bund.zrb.msdosgames.application.usecase;

import de.bund.zrb.msdosgames.application.port.DownloadProgressListener;
import de.bund.zrb.msdosgames.application.port.GameDownloader;
import de.bund.zrb.msdosgames.application.port.LicenseAcceptanceStore;
import de.bund.zrb.msdosgames.domain.DownloadRequest;
import de.bund.zrb.msdosgames.domain.GameFile;
import de.bund.zrb.msdosgames.domain.GameIdentifier;
import de.bund.zrb.msdosgames.domain.LicenseNotice;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DownloadGameUseCaseTest {

    @Test
    void blocksDownloadWhenLicenseWasNotAccepted() throws Exception {
        RecordingDownloader downloader = new RecordingDownloader();
        DownloadGameUseCase useCase = new DownloadGameUseCase(new RejectingLicenseStore(), downloader);

        assertThrows(LicenseNotAcceptedException.class, new org.junit.jupiter.api.function.Executable() {
            @Override
            public void execute() throws Throwable {
                useCase.downloadAcceptedGame(
                        GameIdentifier.of("doom"),
                        new LicenseNotice("", "rights", "source"),
                        new GameFile("doom.zip", "ZIP", 42L, "", ""),
                        new File("build/test-downloads"),
                        null);
            }
        });

        assertFalse(downloader.wasCalled());
    }

    private static final class RejectingLicenseStore implements LicenseAcceptanceStore {
        @Override
        public boolean hasAccepted(GameIdentifier identifier, LicenseNotice licenseNotice) {
            return false;
        }

        @Override
        public void accept(GameIdentifier identifier, LicenseNotice licenseNotice) {
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
