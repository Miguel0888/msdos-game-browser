package com.aresstack.msdosgames.infrastructure.archive;

import com.aresstack.msdosgames.domain.ArchiveItemNotice;
import com.aresstack.msdosgames.domain.ArchiveMetadataEntry;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

final class ArchiveItemPageFactsParser {

    ArchiveItemNotice parse(String html, String sourceUrl) {
        if (html == null || html.trim().length() == 0) {
            return fallbackNotice(sourceUrl);
        }

        Document document = Jsoup.parse(html, sourceUrl);
        String pageTitle = document.title();
        String pageText = document.text();
        boolean hasDownloadOptions = containsIgnoreCase(pageText, "DOWNLOAD OPTIONS")
                || containsIgnoreCase(pageText, "Download Options");
        boolean streamOnly = containsIgnoreCase(pageTitle, "Streaming Only")
                || containsIgnoreCase(pageTitle, "Free Borrow & Streaming")
                || containsIgnoreCase(pageText, "Stream Only")
                || containsIgnoreCase(pageText, "Streaming Only");
        String availability = determineAvailability(pageTitle, pageText, hasDownloadOptions, streamOnly);
        String accessText = determineAccessText(availability, hasDownloadOptions, streamOnly);
        String termsUrl = findTermsUrl(document);
        List<ArchiveMetadataEntry> metadataEntries = parseMetadataEntries(document);

        return new ArchiveItemNotice(
                availability,
                accessText,
                sourceUrl,
                termsUrl,
                streamOnly,
                hasDownloadOptions,
                metadataEntries);
    }

    private ArchiveItemNotice fallbackNotice(String sourceUrl) {
        return new ArchiveItemNotice(
                "Keine eindeutige Verfügbarkeitsangabe erkannt.",
                "Bitte prüfe die Archive.org-Detailseite für verbindliche Zugriffs- und Nutzungshinweise.",
                sourceUrl,
                "",
                false,
                false,
                new ArrayList<ArchiveMetadataEntry>());
    }

    private String determineAvailability(String pageTitle, String pageText, boolean hasDownloadOptions, boolean streamOnly) {
        String titleAvailability = extractAvailabilityFromTitle(pageTitle);
        if (titleAvailability.length() > 0) {
            return titleAvailability;
        }
        if (hasDownloadOptions) {
            return "Download Options";
        }
        if (streamOnly) {
            return "Streaming Only";
        }
        if (containsIgnoreCase(pageText, "Borrow")) {
            return "Borrow/Streaming";
        }
        return "Keine eindeutige Verfügbarkeitsangabe erkannt.";
    }

    private String extractAvailabilityFromTitle(String title) {
        if (title == null) {
            return "";
        }
        String[] parts = title.split(" : ");
        if (parts.length >= 3 && containsIgnoreCase(parts[parts.length - 1], "Internet Archive")) {
            return parts[parts.length - 2].trim();
        }
        return "";
    }

    private String determineAccessText(String availability, boolean hasDownloadOptions, boolean streamOnly) {
        StringBuilder text = new StringBuilder();
        text.append("Archive.org zeigt für dieses Item: ").append(availability).append('.');
        if (hasDownloadOptions) {
            text.append(" Die Detailseite enthält Download-Optionen.");
        }
        if (streamOnly) {
            text.append(" Die Detailseite weist auf Streaming/Borrowing hin; prüfe vor einem lokalen Download die Archive.org-Seite.");
        }
        if (!hasDownloadOptions && !streamOnly) {
            text.append(" Prüfe die Detailseite, falls die lokale Darstellung nicht alle Hinweise enthält.");
        }
        return text.toString();
    }

    private String findTermsUrl(Document document) {
        Element termsLink = document.selectFirst("a[href*=/about/terms], a[href*=terms.php], a[href*=terms]");
        if (termsLink == null) {
            return "";
        }
        return termsLink.absUrl("href");
    }

    private List<ArchiveMetadataEntry> parseMetadataEntries(Document document) {
        List<ArchiveMetadataEntry> entries = new ArrayList<ArchiveMetadataEntry>();
        Elements definitions = document.select("dl.metadata-definition");
        for (Element definition : definitions) {
            Elements names = definition.select("dt");
            Elements values = definition.select("dd");
            int count = Math.min(names.size(), values.size());
            for (int index = 0; index < count; index++) {
                String name = normalizeText(names.get(index).text());
                String value = normalizeText(values.get(index).text());
                if (name.length() > 0 && value.length() > 0) {
                    entries.add(new ArchiveMetadataEntry(name, value));
                }
            }
        }
        return entries;
    }

    private boolean containsIgnoreCase(String text, String value) {
        return text != null && text.toLowerCase().contains(value.toLowerCase());
    }

    private String normalizeText(String value) {
        if (value == null) {
            return "";
        }
        return value.replace('\u00a0', ' ').replaceAll("\\s+", " ").trim();
    }
}
