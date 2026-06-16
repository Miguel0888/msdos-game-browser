package de.bund.zrb.msdosgames.infrastructure.local;

import de.bund.zrb.msdosgames.application.port.LicenseAcceptanceStore;
import de.bund.zrb.msdosgames.domain.GameIdentifier;
import de.bund.zrb.msdosgames.domain.LicenseNotice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

public final class FileBasedLicenseAcceptanceStore implements LicenseAcceptanceStore {

    private final File storeFile;

    public FileBasedLicenseAcceptanceStore(File storeFile) {
        if (storeFile == null) {
            throw new IllegalArgumentException("storeFile must not be null");
        }
        this.storeFile = storeFile;
    }

    @Override
    public synchronized boolean hasAccepted(GameIdentifier identifier, LicenseNotice licenseNotice) throws IOException {
        Properties properties = loadProperties();
        String keyPrefix = createKeyPrefix(identifier);
        String acceptedSignature = properties.getProperty(keyPrefix + ".signature", "");
        return acceptedSignature.equals(createSignature(licenseNotice));
    }

    @Override
    public synchronized void accept(GameIdentifier identifier, LicenseNotice licenseNotice) throws IOException {
        Properties properties = loadProperties();
        String keyPrefix = createKeyPrefix(identifier);
        properties.setProperty(keyPrefix + ".signature", createSignature(licenseNotice));
        properties.setProperty(keyPrefix + ".acceptedAtMillis", String.valueOf(System.currentTimeMillis()));
        properties.setProperty(keyPrefix + ".sourceUrl", licenseNotice.getSourceUrl());
        saveProperties(properties);
    }

    private Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        if (!storeFile.isFile()) {
            return properties;
        }

        FileInputStream inputStream = new FileInputStream(storeFile);
        try {
            properties.load(inputStream);
            return properties;
        } finally {
            inputStream.close();
        }
    }

    private void saveProperties(Properties properties) throws IOException {
        File parentDirectory = storeFile.getParentFile();
        if (!parentDirectory.isDirectory() && !parentDirectory.mkdirs()) {
            throw new IOException("Cannot create directory " + parentDirectory);
        }

        FileOutputStream outputStream = new FileOutputStream(storeFile);
        try {
            properties.store(outputStream, "MS-DOS game browser license acceptances");
        } finally {
            outputStream.close();
        }
    }

    private String createKeyPrefix(GameIdentifier identifier) {
        return "game." + identifier.getValue().replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    private String createSignature(LicenseNotice licenseNotice) throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(licenseNotice.toSignatureSource().getBytes(StandardCharsets.UTF_8));
            return toHex(hash);
        } catch (NoSuchAlgorithmException exception) {
            throw new IOException("SHA-256 is not available", exception);
        }
    }

    private String toHex(byte[] bytes) {
        StringBuilder text = new StringBuilder();
        for (byte value : bytes) {
            String hex = Integer.toHexString(value & 0xff);
            if (hex.length() == 1) {
                text.append('0');
            }
            text.append(hex);
        }
        return text.toString();
    }
}
