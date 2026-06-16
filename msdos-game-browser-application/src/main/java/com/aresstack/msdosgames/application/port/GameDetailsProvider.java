package com.aresstack.msdosgames.application.port;

import com.aresstack.msdosgames.domain.GameDetails;
import com.aresstack.msdosgames.domain.GameIdentifier;

import java.io.IOException;

public interface GameDetailsProvider {

    GameDetails loadDetails(GameIdentifier identifier) throws IOException;
}
