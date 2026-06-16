package de.bund.zrb.msdosgames.frontend.swing;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

final class ExternalUrlOpener {

    boolean canOpenUrls() {
        return Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE);
    }

    void openUrl(String url) throws IOException {
        if (url == null || url.trim().length() == 0) {
            throw new IOException("No URL available.");
        }
        if (!canOpenUrls()) {
            throw new IOException("Opening URLs is not supported on this desktop.");
        }

        try {
            Desktop.getDesktop().browse(URI.create(url.trim()));
        } catch (IllegalArgumentException exception) {
            throw new IOException("Invalid URL: " + url, exception);
        }
    }
}
