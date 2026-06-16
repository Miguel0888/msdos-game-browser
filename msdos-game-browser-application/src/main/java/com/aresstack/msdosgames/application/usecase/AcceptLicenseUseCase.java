package com.aresstack.msdosgames.application.usecase;

import com.aresstack.msdosgames.application.port.ArchiveNoticeAcceptanceStore;
import com.aresstack.msdosgames.domain.GameDetails;

import java.io.IOException;

public final class AcceptLicenseUseCase {

    private final ArchiveNoticeAcceptanceStore acceptanceStore;

    public AcceptLicenseUseCase(ArchiveNoticeAcceptanceStore acceptanceStore) {
        if (acceptanceStore == null) {
            throw new IllegalArgumentException("acceptanceStore must not be null");
        }
        this.acceptanceStore = acceptanceStore;
    }

    public boolean isAccepted(GameDetails gameDetails) throws IOException {
        return acceptanceStore.hasAccepted(gameDetails.getIdentifier(), gameDetails.getArchiveItemNotice());
    }

    public void accept(GameDetails gameDetails) throws IOException {
        acceptanceStore.accept(gameDetails.getIdentifier(), gameDetails.getArchiveItemNotice());
    }
}
