package de.bund.zrb.msdosgames.application.usecase;

import de.bund.zrb.msdosgames.application.port.ArchiveNoticeAcceptanceStore;
import de.bund.zrb.msdosgames.domain.GameDetails;

import java.io.IOException;

public final class AcceptArchiveNoticeUseCase {

    private final ArchiveNoticeAcceptanceStore acceptanceStore;

    public AcceptArchiveNoticeUseCase(ArchiveNoticeAcceptanceStore acceptanceStore) {
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
