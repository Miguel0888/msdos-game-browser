package de.bund.zrb.msdosgames.backend;

import java.util.ArrayList;
import java.util.List;

final class GameDetailsRecord {

    String identifier;
    String title;
    String description;
    String availabilityText;
    String accessText;
    String pageUrl;
    String termsUrl;
    boolean streamOnly;
    boolean downloadOptionsAvailable;
    long itemSize;
    List<MetadataEntryRecord> metadataEntries = new ArrayList<MetadataEntryRecord>();
    List<GameFileRecord> files = new ArrayList<GameFileRecord>();
    List<GameImageRecord> images = new ArrayList<GameImageRecord>();

    static final class MetadataEntryRecord {
        String name;
        String value;
    }

    static final class GameFileRecord {
        String name;
        String format;
        long size;
        String md5;
        String sha1;
    }

    static final class GameImageRecord {
        String title;
        String url;
    }
}
