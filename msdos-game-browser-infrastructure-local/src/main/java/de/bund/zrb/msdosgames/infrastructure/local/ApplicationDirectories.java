package de.bund.zrb.msdosgames.infrastructure.local;

import java.io.File;

public final class ApplicationDirectories {

    private static final String APPLICATION_DIRECTORY_NAME = "msdos-game-browser";

    private ApplicationDirectories() {
    }

    public static File defaultApplicationDirectory() {
        String appDataDirectory = System.getenv("APPDATA");
        if (appDataDirectory != null && appDataDirectory.trim().length() > 0) {
            return new File(appDataDirectory, APPLICATION_DIRECTORY_NAME);
        }
        return new File(System.getProperty("user.home"), "." + APPLICATION_DIRECTORY_NAME);
    }

    public static File defaultLicenseStoreFile() {
        return new File(defaultApplicationDirectory(), "license-acceptance.properties");
    }

    public static File defaultDownloadDirectory() {
        File userHome = new File(System.getProperty("user.home"));
        return new File(new File(userHome, "Downloads"), "msdos-games");
    }
}
