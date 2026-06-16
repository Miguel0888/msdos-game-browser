package de.bund.zrb.msdosgames.infrastructure.archive;

import java.util.List;

final class ArchiveMetadataResponse {

    ArchiveMetadata metadata;
    List<ArchiveFile> files;
    Long item_size;

    static final class ArchiveMetadata {
        String identifier;
        String title;
        String description;
        String creator;
        String date;
        String licenseurl;
        String rights;
    }

    static final class ArchiveFile {
        String name;
        String format;
        String size;
        String md5;
        String sha1;
        String source;
    }
}
