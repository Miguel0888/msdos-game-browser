package de.bund.zrb.msdosgames.domain;

import java.io.File;

public final class DownloadRequest {

    private final GameIdentifier identifier;
    private final GameFile gameFile;
    private final File targetDirectory;

    public DownloadRequest(GameIdentifier identifier, GameFile gameFile, File targetDirectory) {
        if (identifier == null) {
            throw new IllegalArgumentException("identifier must not be null");
        }
        if (gameFile == null) {
            throw new IllegalArgumentException("gameFile must not be null");
        }
        if (targetDirectory == null) {
            throw new IllegalArgumentException("targetDirectory must not be null");
        }
        this.identifier = identifier;
        this.gameFile = gameFile;
        this.targetDirectory = targetDirectory;
    }

    public GameIdentifier getIdentifier() {
        return identifier;
    }

    public GameFile getGameFile() {
        return gameFile;
    }

    public File getTargetDirectory() {
        return targetDirectory;
    }
}
