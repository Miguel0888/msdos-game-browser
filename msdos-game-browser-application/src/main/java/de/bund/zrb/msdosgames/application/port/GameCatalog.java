package de.bund.zrb.msdosgames.application.port;

import de.bund.zrb.msdosgames.domain.GamePage;
import de.bund.zrb.msdosgames.domain.GameSearchCriteria;

import java.io.IOException;

public interface GameCatalog {

    GamePage browse(GameSearchCriteria criteria) throws IOException;

    GamePage search(GameSearchCriteria criteria) throws IOException;
}
