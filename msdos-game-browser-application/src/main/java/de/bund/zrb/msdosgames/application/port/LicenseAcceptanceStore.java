package de.bund.zrb.msdosgames.application.port;

import de.bund.zrb.msdosgames.domain.GameIdentifier;
import de.bund.zrb.msdosgames.domain.ArchiveItemNotice;

import java.io.IOException;

public interface LicenseAcceptanceStore {

    boolean hasAccepted(GameIdentifier identifier, ArchiveItemNotice archiveItemNotice) throws IOException;

    void accept(GameIdentifier identifier, ArchiveItemNotice archiveItemNotice) throws IOException;
}
