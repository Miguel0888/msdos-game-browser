package de.bund.zrb.msdosgames.application.port;

import de.bund.zrb.msdosgames.domain.GameDetails;
import de.bund.zrb.msdosgames.domain.GameIdentifier;

import java.io.IOException;

public interface GameDetailsProvider {

    GameDetails loadDetails(GameIdentifier identifier) throws IOException;
}
