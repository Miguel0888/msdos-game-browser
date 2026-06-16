package de.bund.zrb.msdosgames.frontend.swing;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

final class DownloadDirectoryOpener {

    boolean canOpenDirectory() {
        return Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN);
    }

    void openDirectory(File directory) throws IOException {
        File canonicalDirectory = directory.getCanonicalFile();
        ensureDirectoryExists(canonicalDirectory);
        ensureDirectoryCanBeOpened();
        Desktop.getDesktop().open(canonicalDirectory);
    }

    private void ensureDirectoryExists(File directory) throws IOException {
        if (directory.isDirectory()) {
            return;
        }

        if (!directory.mkdirs()) {
            throw new IOException("Cannot create directory " + directory.getAbsolutePath());
        }
    }

    private void ensureDirectoryCanBeOpened() throws IOException {
        if (!canOpenDirectory()) {
            throw new IOException("Opening directories is not supported on this desktop.");
        }
    }
}
