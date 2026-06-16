package de.bund.zrb.msdosgames.infrastructure.local;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.bund.zrb.msdosgames.application.port.FavoriteGameStore;
import de.bund.zrb.msdosgames.domain.FavoriteGame;
import de.bund.zrb.msdosgames.domain.GameIdentifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class FileBasedFavoriteGameStore implements FavoriteGameStore {

    private final File storeFile;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Map<String, FavoriteGameRecord> favoritesByIdentifier = new LinkedHashMap<String, FavoriteGameRecord>();

    public FileBasedFavoriteGameStore(File storeFile) {
        if (storeFile == null) {
            throw new IllegalArgumentException("storeFile must not be null");
        }
        this.storeFile = storeFile;
        load();
    }

    @Override
    public synchronized List<FavoriteGame> listFavorites() {
        List<FavoriteGame> favorites = new ArrayList<FavoriteGame>();
        for (FavoriteGameRecord record : favoritesByIdentifier.values()) {
            favorites.add(toFavoriteGame(record));
        }
        Collections.sort(favorites, new Comparator<FavoriteGame>() {
            @Override
            public int compare(FavoriteGame left, FavoriteGame right) {
                long diff = right.getAddedAtMillis() - left.getAddedAtMillis();
                if (diff < 0L) {
                    return -1;
                }
                if (diff > 0L) {
                    return 1;
                }
                return left.getTitle().compareToIgnoreCase(right.getTitle());
            }
        });
        return favorites;
    }

    @Override
    public synchronized boolean isFavorite(GameIdentifier identifier) {
        return favoritesByIdentifier.containsKey(identifier.getValue());
    }

    @Override
    public synchronized void addFavorite(FavoriteGame favoriteGame) throws IOException {
        favoritesByIdentifier.put(favoriteGame.getIdentifier().getValue(), toRecord(favoriteGame));
        save();
    }

    @Override
    public synchronized void removeFavorite(GameIdentifier identifier) throws IOException {
        favoritesByIdentifier.remove(identifier.getValue());
        save();
    }

    private void load() {
        if (!storeFile.isFile()) {
            return;
        }

        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(new FileInputStream(storeFile), "UTF-8");
            FavoriteGameDocument document = gson.fromJson(reader, FavoriteGameDocument.class);
            if (document == null || document.favorites == null) {
                return;
            }
            for (FavoriteGameRecord record : document.favorites) {
                if (record != null && record.identifier != null && record.identifier.trim().length() > 0) {
                    favoritesByIdentifier.put(record.identifier.trim(), record);
                }
            }
        } catch (Exception ignored) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
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

        FavoriteGameDocument document = new FavoriteGameDocument();
        document.favorites = new ArrayList<FavoriteGameRecord>(favoritesByIdentifier.values());

        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(storeFile), "UTF-8");
        try {
            gson.toJson(document, writer);
        } finally {
            writer.close();
        }
    }

    private FavoriteGameRecord toRecord(FavoriteGame favoriteGame) {
        FavoriteGameRecord record = new FavoriteGameRecord();
        record.identifier = favoriteGame.getIdentifier().getValue();
        record.title = favoriteGame.getTitle();
        record.description = favoriteGame.getDescription();
        record.creator = favoriteGame.getCreator();
        record.date = favoriteGame.getDate();
        record.sourceUrl = favoriteGame.getSourceUrl();
        record.addedAtMillis = favoriteGame.getAddedAtMillis();
        return record;
    }

    private FavoriteGame toFavoriteGame(FavoriteGameRecord record) {
        return new FavoriteGame(
                GameIdentifier.of(record.identifier),
                record.title,
                record.description,
                record.creator,
                record.date,
                record.sourceUrl,
                record.addedAtMillis);
    }

    private static final class FavoriteGameDocument {
        List<FavoriteGameRecord> favorites = new ArrayList<FavoriteGameRecord>();
    }

    private static final class FavoriteGameRecord {
        String identifier;
        String title;
        String description;
        String creator;
        String date;
        String sourceUrl;
        long addedAtMillis;
    }
}
