package com.aresstack.msdosgames.application.port;

import com.aresstack.msdosgames.domain.GameIdentifier;
import com.aresstack.msdosgames.domain.ArchiveItemNotice;

import java.io.IOException;

public interface LicenseAcceptanceStore {

    boolean hasAccepted(GameIdentifier identifier, ArchiveItemNotice archiveItemNotice) throws IOException;

    void accept(GameIdentifier identifier, ArchiveItemNotice archiveItemNotice) throws IOException;
}
