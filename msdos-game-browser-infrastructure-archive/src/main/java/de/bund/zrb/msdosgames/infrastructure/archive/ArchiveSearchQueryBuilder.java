package de.bund.zrb.msdosgames.infrastructure.archive;

public final class ArchiveSearchQueryBuilder {

    private static final String COLLECTION = "softwarelibrary_msdos_games";

    public String buildBrowseQuery() {
        return "collection:" + COLLECTION;
    }

    public String buildSearchQuery(String searchText) {
        String normalizedText = normalizeSearchText(searchText);
        return "(collection:" + COLLECTION + ") AND (title:\"" + normalizedText + "\" OR description:\"" + normalizedText + "\")";
    }

    private String normalizeSearchText(String searchText) {
        if (searchText == null) {
            throw new IllegalArgumentException("searchText must not be null");
        }

        String trimmedText = searchText.trim();
        if (trimmedText.length() == 0) {
            throw new IllegalArgumentException("searchText must not be blank");
        }

        return trimmedText.replace('"', ' ');
    }
}
