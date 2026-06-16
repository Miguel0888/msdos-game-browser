package com.aresstack.msdosgames.infrastructure.local;

import com.aresstack.msdosgames.domain.ArchiveItemNotice;
import com.aresstack.msdosgames.domain.GameIdentifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileBasedLicenseAcceptanceStoreTest {

    @TempDir
    File temporaryDirectory;

    @Test
    void persistsAcceptanceForSameArchiveItemNotice() throws Exception {
        File storeFile = new File(temporaryDirectory, "acceptance.properties");
        FileBasedLicenseAcceptanceStore store = new FileBasedLicenseAcceptanceStore(storeFile);
        GameIdentifier identifier = GameIdentifier.of("doom");
        ArchiveItemNotice notice = new ArchiveItemNotice("Download Options", "access", "source", "", false, true, Collections.emptyList());

        assertFalse(store.hasAccepted(identifier, notice));

        store.accept(identifier, notice);

        assertTrue(new FileBasedLicenseAcceptanceStore(storeFile).hasAccepted(identifier, notice));
    }
}
