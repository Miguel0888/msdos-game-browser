package de.bund.zrb.msdosgames.application.port;

import de.bund.zrb.msdosgames.domain.GameIdentifier;
import de.bund.zrb.msdosgames.domain.LicenseNotice;

import java.io.IOException;

public interface LicenseAcceptanceStore {

    boolean hasAccepted(GameIdentifier identifier, LicenseNotice licenseNotice) throws IOException;

    void accept(GameIdentifier identifier, LicenseNotice licenseNotice) throws IOException;
}
