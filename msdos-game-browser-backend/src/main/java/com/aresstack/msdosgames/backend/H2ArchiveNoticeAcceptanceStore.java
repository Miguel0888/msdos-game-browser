package com.aresstack.msdosgames.backend;

import com.aresstack.msdosgames.application.port.ArchiveNoticeAcceptanceStore;
import com.aresstack.msdosgames.domain.ArchiveItemNotice;
import com.aresstack.msdosgames.domain.GameIdentifier;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class H2ArchiveNoticeAcceptanceStore implements ArchiveNoticeAcceptanceStore {

    private final Connection connection;

    public H2ArchiveNoticeAcceptanceStore(File databaseDirectory) {
        try {
            File directory = ensureDirectory(databaseDirectory);
            connection = DriverManager.getConnection(createJdbcUrl(directory));
            createSchema();
        } catch (SQLException exception) {
            throw new IllegalStateException("Cannot create archive notice acceptance store", exception);
        }
    }

    @Override
    public synchronized boolean hasAccepted(GameIdentifier identifier, ArchiveItemNotice archiveItemNotice) throws IOException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement("select signature from archive_notice_acceptance where identifier = ?");
            statement.setString(1, identifier.getValue());
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return false;
            }
            return createSignature(archiveItemNotice).equals(resultSet.getString(1));
        } catch (SQLException exception) {
            throw new IOException("Cannot read archive notice acceptance", exception);
        } finally {
            closeQuietly(resultSet);
            closeQuietly(statement);
        }
    }

    @Override
    public synchronized void accept(GameIdentifier identifier, ArchiveItemNotice archiveItemNotice) throws IOException {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("merge into archive_notice_acceptance key(identifier) values (?, ?, ?, ?)");
            statement.setString(1, identifier.getValue());
            statement.setString(2, createSignature(archiveItemNotice));
            statement.setString(3, archiveItemNotice.getSourceUrl());
            statement.setLong(4, System.currentTimeMillis());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IOException("Cannot save archive notice acceptance", exception);
        } finally {
            closeQuietly(statement);
        }
    }

    public synchronized void close() {
        closeQuietly(connection);
    }

    private void createSchema() throws SQLException {
        Statement statement = connection.createStatement();
        try {
            statement.executeUpdate("create table if not exists archive_notice_acceptance (identifier varchar(512) primary key, signature varchar(64) not null, source_url varchar(2048), accepted_at_millis bigint not null)");
        } finally {
            statement.close();
        }
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

    private File ensureDirectory(File directory) {
        if (directory == null) {
            throw new IllegalArgumentException("databaseDirectory must not be null");
        }
        if (directory.isDirectory()) {
            return directory;
        }
        if (!directory.mkdirs() && !directory.isDirectory()) {
            throw new IllegalStateException("Cannot create database directory " + directory.getAbsolutePath());
        }
        return directory;
    }

    private String createJdbcUrl(File databaseDirectory) {
        File databaseFile = new File(databaseDirectory, "msdos-game-browser");
        String path = databaseFile.getAbsolutePath().replace('\\', '/');
        return "jdbc:h2:file:" + path + ";AUTO_SERVER=FALSE";
    }

    private void closeQuietly(AutoCloseable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (Exception ignored) {
        }
    }
}
