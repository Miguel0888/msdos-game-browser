package com.aresstack.msdosgames.application.port;

import com.aresstack.msdosgames.domain.ArchiveItemNotice;
import com.aresstack.msdosgames.domain.GameIdentifier;

import java.io.IOException;

public interface ArchiveNoticeAcceptanceStore {

    boolean hasAccepted(GameIdentifier identifier, ArchiveItemNotice archiveItemNotice) throws IOException;

    void accept(GameIdentifier identifier, ArchiveItemNotice archiveItemNotice) throws IOException;
}
