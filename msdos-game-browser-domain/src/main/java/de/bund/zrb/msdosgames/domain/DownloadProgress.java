package de.bund.zrb.msdosgames.domain;

public final class DownloadProgress {

    private final String fileName;
    private final long bytesRead;
    private final long totalBytes;
    private final boolean finished;

    public DownloadProgress(String fileName, long bytesRead, long totalBytes, boolean finished) {
        this.fileName = fileName == null ? "" : fileName;
        this.bytesRead = Math.max(0L, bytesRead);
        this.totalBytes = Math.max(0L, totalBytes);
        this.finished = finished;
    }

    public String getFileName() {
        return fileName;
    }

    public long getBytesRead() {
        return bytesRead;
    }

    public long getTotalBytes() {
        return totalBytes;
    }

    public boolean isFinished() {
        return finished;
    }

    public int getPercent() {
        if (totalBytes <= 0L) {
            return 0;
        }
        long percent = bytesRead * 100L / totalBytes;
        return (int) Math.min(100L, Math.max(0L, percent));
    }
}
