package com.aresstack.msdosgames.ui;

import com.aresstack.msdosgames.backend.MsdosGameBrowserBackend;
import com.aresstack.msdosgames.frontend.swing.GameBrowserFrame;

import javax.swing.SwingUtilities;

public final class MsdosGameBrowserApplication {

    private MsdosGameBrowserApplication() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MsdosGameBrowserBackend backend = MsdosGameBrowserBackend.createDefault();
                createFrame(backend).showBrowser();
            }
        });
    }

    private static GameBrowserFrame createFrame(MsdosGameBrowserBackend backend) {
        return new GameBrowserFrame(
                backend.getBrowserService(),
                backend.getAcceptArchiveNoticeUseCase(),
                backend.getDownloadGameUseCase(),
                backend.getFavoriteGamesUseCase(),
                backend.getApplicationDirectory(),
                backend.getDownloadDirectory());
    }
}
