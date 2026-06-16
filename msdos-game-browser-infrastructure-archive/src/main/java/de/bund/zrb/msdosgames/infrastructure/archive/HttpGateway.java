package de.bund.zrb.msdosgames.infrastructure.archive;

import java.io.IOException;

public interface HttpGateway {

    String getText(String url) throws IOException;
}
