package de.bund.zrb.msdosgames.application.usecase;

import de.bund.zrb.msdosgames.domain.GameIdentifier;

import java.io.IOException;

public final class ArchiveNoticeNotAcceptedException extends IOException {

    public ArchiveNoticeNotAcceptedException(GameIdentifier identifier) {
        super("Archive.org notice was not accepted for " + identifier.getValue());
    }
}
