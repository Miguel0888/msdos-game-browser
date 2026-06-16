package com.aresstack.msdosgames.infrastructure.archive;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArchiveSearchQueryBuilderTest {

    @Test
    void buildsCollectionBrowseQuery() {
        assertEquals("collection:softwarelibrary_msdos_games", new ArchiveSearchQueryBuilder().buildBrowseQuery());
    }

    @Test
    void buildsCollectionScopedSearchQuery() {
        String query = new ArchiveSearchQueryBuilder().buildSearchQuery("doom");

        assertTrue(query.contains("collection:softwarelibrary_msdos_games"));
        assertTrue(query.contains("title:\"doom\""));
        assertTrue(query.contains("description:\"doom\""));
    }
}
