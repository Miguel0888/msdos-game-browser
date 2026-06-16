package com.aresstack.msdosgames.application.port;

import com.aresstack.msdosgames.domain.DownloadRequest;

import java.io.IOException;

public interface GameDownloader {

    void download(DownloadRequest request, DownloadProgressListener progressListener) throws IOException;
}
