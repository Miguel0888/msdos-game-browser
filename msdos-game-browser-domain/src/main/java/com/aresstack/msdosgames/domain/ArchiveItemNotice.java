package com.aresstack.msdosgames.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ArchiveItemNotice {

    private final String availabilityText;
    private final String accessText;
    private final String sourceUrl;
    private final String termsUrl;
    private final boolean streamOnly;
    private final boolean downloadOptionsAvailable;
    private final List<ArchiveMetadataEntry> metadataEntries;

    public ArchiveItemNotice(
            String availabilityText,
            String accessText,
            String sourceUrl,
            String termsUrl,
            boolean streamOnly,
            boolean downloadOptionsAvailable,
            List<ArchiveMetadataEntry> metadataEntries) {
        this.availabilityText = textOrFallback(availabilityText, "Keine eindeutige Verfügbarkeitsangabe erkannt.");
        this.accessText = textOrFallback(accessText, "Bitte prüfe die Archive.org-Detailseite für verbindliche Zugriffs- und Nutzungshinweise.");
        this.sourceUrl = textOrEmpty(sourceUrl);
        this.termsUrl = textOrEmpty(termsUrl);
        this.streamOnly = streamOnly;
        this.downloadOptionsAvailable = downloadOptionsAvailable;
        if (metadataEntries == null) {
            this.metadataEntries = new ArrayList<ArchiveMetadataEntry>();
        } else {
            this.metadataEntries = new ArrayList<ArchiveMetadataEntry>(metadataEntries);
        }
    }

    public String getAvailabilityText() {
        return availabilityText;
    }

    public String getAccessText() {
        return accessText;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public String getTermsUrl() {
        return termsUrl;
    }

    public boolean isStreamOnly() {
        return streamOnly;
    }

    public boolean hasDownloadOptionsAvailable() {
        return downloadOptionsAvailable;
    }

    public List<ArchiveMetadataEntry> getMetadataEntries() {
        return Collections.unmodifiableList(metadataEntries);
    }

    public String toSignatureSource() {
        StringBuilder signature = new StringBuilder();
        signature.append(availabilityText).append('\n');
        signature.append(accessText).append('\n');
        signature.append(sourceUrl).append('\n');
        signature.append(termsUrl).append('\n');
        signature.append(streamOnly).append('\n');
        signature.append(downloadOptionsAvailable).append('\n');
        for (ArchiveMetadataEntry entry : metadataEntries) {
            signature.append(entry.getName()).append('=').append(entry.getValue()).append('\n');
        }
        return signature.toString();
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
