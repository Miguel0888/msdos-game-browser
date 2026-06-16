package de.bund.zrb.msdosgames.application.usecase;

import de.bund.zrb.msdosgames.domain.GameIdentifier;

import java.io.IOException;

public final class LicenseNotAcceptedException extends IOException {

    public LicenseNotAcceptedException(GameIdentifier identifier) {
        super("License notice has not been accepted for " + identifier.getValue());
    }
}
