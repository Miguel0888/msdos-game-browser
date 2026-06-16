package com.aresstack.msdosgames.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class GameDetails {

    private final GameIdentifier identifier;
    private final String title;
    private final String descriptionText;
    private final ArchiveItemNotice archiveItemNotice;
    private final List<GameFile> downloadableFiles;
    private final List<GameImage> previewImages;
    private final long itemSize;

    public GameDetails(
            GameIdentifier identifier,
            String title,
            String descriptionText,
            ArchiveItemNotice archiveItemNotice,
            List<GameFile> downloadableFiles,
            List<GameImage> previewImages,
            long itemSize) {
        if (identifier == null) {
            throw new IllegalArgumentException("identifier must not be null");
        }
        if (archiveItemNotice == null) {
            throw new IllegalArgumentException("archiveItemNotice must not be null");
        }
        if (downloadableFiles == null) {
            throw new IllegalArgumentException("downloadableFiles must not be null");
        }
        if (previewImages == null) {
            throw new IllegalArgumentException("previewImages must not be null");
        }
        this.identifier = identifier;
        this.title = textOrFallback(title, identifier.getValue());
        this.descriptionText = textOrFallback(descriptionText, "Keine Beschreibung vorhanden.");
        this.archiveItemNotice = archiveItemNotice;
        this.downloadableFiles = new ArrayList<GameFile>(downloadableFiles);
        this.previewImages = new ArrayList<GameImage>(previewImages);
        this.itemSize = Math.max(0L, itemSize);
    }

    public GameIdentifier getIdentifier() {
        return identifier;
    }

    public String getTitle() {
        return title;
    }

    public String getDescriptionText() {
        return descriptionText;
    }

    public ArchiveItemNotice getArchiveItemNotice() {
        return archiveItemNotice;
    }

    public List<GameFile> getDownloadableFiles() {
        return Collections.unmodifiableList(downloadableFiles);
    }

    public List<GameImage> getPreviewImages() {
        return Collections.unmodifiableList(previewImages);
    }

    public long getItemSize() {
        return itemSize;
    }

    public boolean hasDownloadableFiles() {
        return !downloadableFiles.isEmpty();
    }

    private static String textOrFallback(String value, String fallback) {
        String cleanedValue = textOrEmpty(value);
        if (cleanedValue.length() == 0) {
            return fallback;
        }
        return cleanedValue;
    }

    private static String textOrEmpty(String value) {
        if (value == null) {
            return "";
        }
        return value.trim();
    }
}
