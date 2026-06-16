package de.bund.zrb.msdosgames.infrastructure.archive;

final class ArchiveFileFilter {

    boolean accepts(ArchiveMetadataResponse.ArchiveFile file) {
        if (file == null || file.name == null || file.name.trim().length() == 0) {
            return false;
        }

        String lowerName = file.name.toLowerCase();
        if (lowerName.endsWith("_meta.xml")
                || lowerName.endsWith("_files.xml")
                || lowerName.endsWith("_reviews.xml")
                || lowerName.endsWith("_meta.sqlite")
                || lowerName.endsWith("_thumbs.zip")
                || lowerName.endsWith(".torrent")) {
            return false;
        }

        String lowerFormat = file.format == null ? "" : file.format.toLowerCase();
        if (lowerFormat.contains("metadata") || lowerFormat.contains("item tile")) {
            return false;
        }

        return true;
    }
}
