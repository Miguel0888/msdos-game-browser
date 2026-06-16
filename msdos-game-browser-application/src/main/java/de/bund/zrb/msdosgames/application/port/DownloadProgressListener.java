package de.bund.zrb.msdosgames.application.port;

import de.bund.zrb.msdosgames.domain.DownloadProgress;

public interface DownloadProgressListener {

    void onProgress(DownloadProgress progress);
}
