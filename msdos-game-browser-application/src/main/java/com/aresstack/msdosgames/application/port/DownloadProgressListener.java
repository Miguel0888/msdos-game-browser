package com.aresstack.msdosgames.application.port;

import com.aresstack.msdosgames.domain.DownloadProgress;

public interface DownloadProgressListener {

    void onProgress(DownloadProgress progress);
}
