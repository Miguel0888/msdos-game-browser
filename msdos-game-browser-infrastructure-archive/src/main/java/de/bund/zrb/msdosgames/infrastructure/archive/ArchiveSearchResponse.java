package de.bund.zrb.msdosgames.infrastructure.archive;

import java.util.List;

final class ArchiveSearchResponse {

    List<ArchiveSearchItem> items;
    String cursor;

    static final class ArchiveSearchItem {
        String identifier;
        String title;
        String description;
        String creator;
        String date;
        String publicdate;
        Long downloads;
        Long item_size;
    }
}
