package de.bund.zrb.msdosgames.application.port;

import de.bund.zrb.msdosgames.domain.ArchiveItemNotice;
import de.bund.zrb.msdosgames.domain.GameIdentifier;

import java.io.IOException;

public interface ArchiveNoticeAcceptanceStore {

    boolean hasAccepted(GameIdentifier identifier, ArchiveItemNotice archiveItemNotice) throws IOException;

    void accept(GameIdentifier identifier, ArchiveItemNotice archiveItemNotice) throws IOException;
}
