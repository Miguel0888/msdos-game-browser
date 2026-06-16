package de.bund.zrb.msdosgames.infrastructure.archive;

import de.bund.zrb.msdosgames.application.port.DownloadProgressListener;
import de.bund.zrb.msdosgames.application.port.GameDownloader;
import de.bund.zrb.msdosgames.domain.DownloadProgress;
import de.bund.zrb.msdosgames.domain.DownloadRequest;
import de.bund.zrb.msdosgames.domain.GameFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public final class InternetArchiveDownloadClient implements GameDownloader {

    private static final int BUFFER_SIZE = 64 * 1024;
    private static final int CONNECT_TIMEOUT_MILLIS = 15000;
    private static final int READ_TIMEOUT_MILLIS = 120000;

    private final InternetArchiveUrlBuilder urlBuilder;

    public InternetArchiveDownloadClient() {
        this(new InternetArchiveUrlBuilder());
    }

    InternetArchiveDownloadClient(InternetArchiveUrlBuilder urlBuilder) {
        this.urlBuilder = urlBuilder;
    }

    @Override
    public void download(DownloadRequest request, DownloadProgressListener progressListener) throws IOException {
        GameFile gameFile = request.getGameFile();
        String downloadUrl = urlBuilder.buildDownloadUrl(request.getIdentifier(), gameFile.getName());
        File targetFile = createSafeTargetFile(request);
        File partialFile = new File(targetFile.getParentFile(), targetFile.getName() + ".part");

        if (!targetFile.getParentFile().isDirectory() && !targetFile.getParentFile().mkdirs()) {
            throw new IOException("Cannot create download directory " + targetFile.getParentFile());
        }

        HttpURLConnection connection = openDownloadConnection(downloadUrl);
        int statusCode = connection.getResponseCode();
        if (statusCode < 200 || statusCode >= 300) {
            throw new IOException("HTTP " + statusCode + " while downloading " + downloadUrl);
        }

        long totalBytes = connection.getContentLengthLong();
        copyResponseToFile(connection.getInputStream(), partialFile, gameFile.getName(), totalBytes, progressListener);
        replaceTargetFile(partialFile, targetFile);
        notifyProgress(progressListener, new DownloadProgress(gameFile.getName(), targetFile.length(), totalBytes, true));
    }

    private HttpURLConnection openDownloadConnection(String downloadUrl) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(downloadUrl).openConnection();
        connection.setConnectTimeout(CONNECT_TIMEOUT_MILLIS);
        connection.setReadTimeout(READ_TIMEOUT_MILLIS);
        connection.setRequestProperty("User-Agent", "msdos-game-browser/1.0 (+https://archive.org)");
        return connection;
    }

    private void copyResponseToFile(
            InputStream inputStream,
            File partialFile,
            String fileName,
            long totalBytes,
            DownloadProgressListener progressListener) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        long bytesRead = 0L;
        FileOutputStream outputStream = new FileOutputStream(partialFile);
        try {
            int read;
            while ((read = inputStream.read(buffer)) >= 0) {
                outputStream.write(buffer, 0, read);
                bytesRead += read;
                notifyProgress(progressListener, new DownloadProgress(fileName, bytesRead, totalBytes, false));
            }
        } finally {
            try {
                outputStream.close();
            } finally {
                inputStream.close();
            }
        }
    }

    private File createSafeTargetFile(DownloadRequest request) throws IOException {
        File itemDirectory = new File(request.getTargetDirectory(), sanitizeName(request.getIdentifier().getValue()));
        File targetFile = new File(itemDirectory, sanitizeRelativePath(request.getGameFile().getName()));
        File canonicalItemDirectory = itemDirectory.getCanonicalFile();
        File canonicalTargetFile = targetFile.getCanonicalFile();
        if (!canonicalTargetFile.getPath().startsWith(canonicalItemDirectory.getPath())) {
            throw new IOException("Unsafe archive file path: " + request.getGameFile().getName());
        }
        return canonicalTargetFile;
    }

    private String sanitizeRelativePath(String fileName) {
        return fileName.replace('\\', '/').replace("..", "_");
    }

    private String sanitizeName(String value) {
        return value.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    private void replaceTargetFile(File partialFile, File targetFile) throws IOException {
        if (targetFile.exists() && !targetFile.delete()) {
            throw new IOException("Cannot replace existing file " + targetFile);
        }
        if (!partialFile.renameTo(targetFile)) {
            throw new IOException("Cannot move partial download to " + targetFile);
        }
    }

    private void notifyProgress(DownloadProgressListener progressListener, DownloadProgress progress) {
        if (progressListener != null) {
            progressListener.onProgress(progress);
        }
    }
}
