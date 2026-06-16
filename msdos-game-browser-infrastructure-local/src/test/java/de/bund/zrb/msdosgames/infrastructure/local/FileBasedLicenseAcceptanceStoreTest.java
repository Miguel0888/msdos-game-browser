package de.bund.zrb.msdosgames.infrastructure.local;

import de.bund.zrb.msdosgames.domain.GameIdentifier;
import de.bund.zrb.msdosgames.domain.LicenseNotice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileBasedLicenseAcceptanceStoreTest {

    @TempDir
    File temporaryDirectory;

    @Test
    void persistsAcceptanceForSameLicenseNotice() throws Exception {
        File storeFile = new File(temporaryDirectory, "acceptance.properties");
        FileBasedLicenseAcceptanceStore store = new FileBasedLicenseAcceptanceStore(storeFile);
        GameIdentifier identifier = GameIdentifier.of("doom");
        LicenseNotice licenseNotice = new LicenseNotice("license", "rights", "source");

        assertFalse(store.hasAccepted(identifier, licenseNotice));

        store.accept(identifier, licenseNotice);

        assertTrue(new FileBasedLicenseAcceptanceStore(storeFile).hasAccepted(identifier, licenseNotice));
    }
}
