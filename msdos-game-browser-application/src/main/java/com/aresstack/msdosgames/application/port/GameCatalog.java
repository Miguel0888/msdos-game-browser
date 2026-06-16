package com.aresstack.msdosgames.application.port;

import com.aresstack.msdosgames.domain.GamePage;
import com.aresstack.msdosgames.domain.GameSearchCriteria;

import java.io.IOException;

public interface GameCatalog {

    GamePage browse(GameSearchCriteria criteria) throws IOException;

    GamePage search(GameSearchCriteria criteria) throws IOException;
}
