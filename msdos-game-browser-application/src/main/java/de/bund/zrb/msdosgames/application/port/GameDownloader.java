package de.bund.zrb.msdosgames.application.port;

import de.bund.zrb.msdosgames.domain.DownloadRequest;

import java.io.IOException;

public interface GameDownloader {

    void download(DownloadRequest request, DownloadProgressListener progressListener) throws IOException;
}
