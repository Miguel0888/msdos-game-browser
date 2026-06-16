package de.bund.zrb.msdosgames.infrastructure.local;

import de.bund.zrb.msdosgames.application.port.ArchiveNoticeAcceptanceStore;
import de.bund.zrb.msdosgames.domain.ArchiveItemNotice;
import de.bund.zrb.msdosgames.domain.GameIdentifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

public final class FileBasedLicenseAcceptanceStore implements ArchiveNoticeAcceptanceStore {

    private final File storeFile;
    private final Properties properties = new Properties();

    public FileBasedLicenseAcceptanceStore(File storeFile) {
        if (storeFile == null) {
            throw new IllegalArgumentException("storeFile must not be null");
        }
        this.storeFile = storeFile;
        load();
    }

    @Override
    public synchronized boolean hasAccepted(GameIdentifier identifier, ArchiveItemNotice archiveItemNotice) throws IOException {
        String key = createKey(identifier);
        String expectedSignature = createSignature(archiveItemNotice);
        return expectedSignature.equals(properties.getProperty(key + ".signature"));
    }

    @Override
    public synchronized void accept(GameIdentifier identifier, ArchiveItemNotice archiveItemNotice) throws IOException {
        String key = createKey(identifier);
        properties.setProperty(key + ".signature", createSignature(archiveItemNotice));
        properties.setProperty(key + ".acceptedAtMillis", String.valueOf(System.currentTimeMillis()));
        properties.setProperty(key + ".sourceUrl", archiveItemNotice.getSourceUrl());
        save();
    }

    private void load() {
        if (!storeFile.isFile()) {
            return;
        }

        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(storeFile);
            properties.load(inputStream);
        } catch (IOException ignored) {
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    private void save() throws IOException {
        File parent = storeFile.getParentFile();
        if (parent != null && !parent.isDirectory() && !parent.mkdirs()) {
            throw new IOException("Cannot create directory " + parent.getAbsolutePath());
        }

        FileOutputStream outputStream = new FileOutputStream(storeFile);
        try {
            properties.store(outputStream, "MS-DOS Game Browser archive item notice acceptances");
        } finally {
            outputStream.close();
        }
    }

    private String createKey(GameIdentifier identifier) {
        return "game." + identifier.getValue().replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    private String createSignature(ArchiveItemNotice archiveItemNotice) throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(archiveItemNotice.toSignatureSource().getBytes("UTF-8"));
            StringBuilder builder = new StringBuilder();
            for (byte value : hash) {
                builder.append(String.format("%02x", value & 0xff));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException exception) {
            throw new IOException("SHA-256 is not available", exception);
        }
    }
}
