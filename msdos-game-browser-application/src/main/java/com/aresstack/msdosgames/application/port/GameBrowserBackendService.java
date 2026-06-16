package com.aresstack.msdosgames.application.port;

import com.aresstack.msdosgames.domain.GameDetails;
import com.aresstack.msdosgames.domain.GameIdentifier;
import com.aresstack.msdosgames.domain.GameImage;
import com.aresstack.msdosgames.domain.GamePage;
import com.aresstack.msdosgames.domain.GameSearchCriteria;
import com.aresstack.msdosgames.domain.GameSummary;

import java.io.IOException;
import java.util.List;

public interface GameBrowserBackendService {

    GamePage browse(GameSearchCriteria criteria) throws IOException;

    GamePage search(GameSearchCriteria criteria) throws IOException;

    GameDetails loadDetailsNow(GameIdentifier identifier) throws Exception;

    void preloadDetails(List<GameSummary> games, int firstIndex, int lastIndex);

    byte[] loadPreviewImageNow(GameImage image) throws Exception;

    void cancelLowPriorityImagePreloading();

    void clearDatabaseCache() throws Exception;

    void shutdown();
}
