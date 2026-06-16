package com.aresstack.msdosgames.application.usecase;

import com.aresstack.msdosgames.domain.GameIdentifier;

import java.io.IOException;

public final class LicenseNotAcceptedException extends IOException {

    public LicenseNotAcceptedException(GameIdentifier identifier) {
        super("License notice has not been accepted for " + identifier.getValue());
    }
}
