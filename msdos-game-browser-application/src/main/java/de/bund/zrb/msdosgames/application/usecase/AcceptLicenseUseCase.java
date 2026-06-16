package de.bund.zrb.msdosgames.application.usecase;

import de.bund.zrb.msdosgames.application.port.LicenseAcceptanceStore;
import de.bund.zrb.msdosgames.domain.GameDetails;

import java.io.IOException;

public final class AcceptLicenseUseCase {

    private final LicenseAcceptanceStore licenseAcceptanceStore;

    public AcceptLicenseUseCase(LicenseAcceptanceStore licenseAcceptanceStore) {
        if (licenseAcceptanceStore == null) {
            throw new IllegalArgumentException("licenseAcceptanceStore must not be null");
        }
        this.licenseAcceptanceStore = licenseAcceptanceStore;
    }

    public boolean isAccepted(GameDetails gameDetails) throws IOException {
        return licenseAcceptanceStore.hasAccepted(gameDetails.getIdentifier(), gameDetails.getArchiveItemNotice());
    }

    public void accept(GameDetails gameDetails) throws IOException {
        licenseAcceptanceStore.accept(gameDetails.getIdentifier(), gameDetails.getArchiveItemNotice());
    }
}
