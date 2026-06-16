package de.bund.zrb.msdosgames.backend;

import de.bund.zrb.msdosgames.domain.ArchiveItemNotice;
import de.bund.zrb.msdosgames.domain.ArchiveMetadataEntry;
import de.bund.zrb.msdosgames.domain.GameDetails;
import de.bund.zrb.msdosgames.domain.GameFile;
import de.bund.zrb.msdosgames.domain.GameIdentifier;
import de.bund.zrb.msdosgames.domain.GameImage;

import java.util.ArrayList;
import java.util.List;

final class GameDetailsRecordMapper {

    GameDetailsRecord toRecord(GameDetails details) {
        GameDetailsRecord record = new GameDetailsRecord();
        record.identifier = details.getIdentifier().getValue();
        record.title = details.getTitle();
        record.description = details.getDescriptionText();
        record.availabilityText = details.getArchiveItemNotice().getAvailabilityText();
        record.accessText = details.getArchiveItemNotice().getAccessText();
        record.pageUrl = details.getArchiveItemNotice().getSourceUrl();
        record.termsUrl = details.getArchiveItemNotice().getTermsUrl();
        record.streamOnly = details.getArchiveItemNotice().isStreamOnly();
        record.downloadOptionsAvailable = details.getArchiveItemNotice().hasDownloadOptionsAvailable();
        record.metadataEntries = toMetadataEntryRecords(details.getArchiveItemNotice().getMetadataEntries());
        record.itemSize = details.getItemSize();
        record.files = toFileRecords(details.getDownloadableFiles());
        record.images = toImageRecords(details.getPreviewImages());
        return record;
    }

    GameDetails toDetails(GameDetailsRecord record) {
        return new GameDetails(
                GameIdentifier.of(record.identifier),
                record.title,
                record.description,
                new ArchiveItemNotice(
                        record.availabilityText,
                        record.accessText,
                        record.pageUrl,
                        record.termsUrl,
                        record.streamOnly,
                        record.downloadOptionsAvailable,
                        toMetadataEntries(record.metadataEntries)),
                toFiles(record.files),
                toImages(record.images),
                record.itemSize);
    }

    private List<GameDetailsRecord.MetadataEntryRecord> toMetadataEntryRecords(List<ArchiveMetadataEntry> entries) {
        List<GameDetailsRecord.MetadataEntryRecord> records = new ArrayList<GameDetailsRecord.MetadataEntryRecord>();
        for (ArchiveMetadataEntry entry : entries) {
            GameDetailsRecord.MetadataEntryRecord record = new GameDetailsRecord.MetadataEntryRecord();
            record.name = entry.getName();
            record.value = entry.getValue();
            records.add(record);
        }
        return records;
    }

    private List<ArchiveMetadataEntry> toMetadataEntries(List<GameDetailsRecord.MetadataEntryRecord> records) {
        List<ArchiveMetadataEntry> entries = new ArrayList<ArchiveMetadataEntry>();
        if (records == null) {
            return entries;
        }
        for (GameDetailsRecord.MetadataEntryRecord record : records) {
            entries.add(new ArchiveMetadataEntry(record.name, record.value));
        }
        return entries;
    }

    private List<GameDetailsRecord.GameFileRecord> toFileRecords(List<GameFile> files) {
        List<GameDetailsRecord.GameFileRecord> records = new ArrayList<GameDetailsRecord.GameFileRecord>();
        for (GameFile file : files) {
            GameDetailsRecord.GameFileRecord record = new GameDetailsRecord.GameFileRecord();
            record.name = file.getName();
            record.format = file.getFormat();
            record.size = file.getSize();
            record.md5 = file.getMd5();
            record.sha1 = file.getSha1();
            records.add(record);
        }
        return records;
    }

    private List<GameDetailsRecord.GameImageRecord> toImageRecords(List<GameImage> images) {
        List<GameDetailsRecord.GameImageRecord> records = new ArrayList<GameDetailsRecord.GameImageRecord>();
        for (GameImage image : images) {
            GameDetailsRecord.GameImageRecord record = new GameDetailsRecord.GameImageRecord();
            record.title = image.getTitle();
            record.url = image.getUrl();
            records.add(record);
        }
        return records;
    }

    private List<GameFile> toFiles(List<GameDetailsRecord.GameFileRecord> records) {
        List<GameFile> files = new ArrayList<GameFile>();
        if (records == null) {
            return files;
        }
        for (GameDetailsRecord.GameFileRecord record : records) {
            files.add(new GameFile(record.name, record.format, record.size, record.md5, record.sha1));
        }
        return files;
    }

    private List<GameImage> toImages(List<GameDetailsRecord.GameImageRecord> records) {
        List<GameImage> images = new ArrayList<GameImage>();
        if (records == null) {
            return images;
        }
        for (GameDetailsRecord.GameImageRecord record : records) {
            images.add(new GameImage(record.title, record.url));
        }
        return images;
    }
}
