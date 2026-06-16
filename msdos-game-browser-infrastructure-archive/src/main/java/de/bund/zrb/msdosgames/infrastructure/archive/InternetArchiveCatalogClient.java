package de.bund.zrb.msdosgames.infrastructure.archive;

import com.google.gson.Gson;
import de.bund.zrb.msdosgames.application.port.GameCatalog;
import de.bund.zrb.msdosgames.application.port.GameDetailsProvider;
import de.bund.zrb.msdosgames.domain.GameDetails;
import de.bund.zrb.msdosgames.domain.GameFile;
import de.bund.zrb.msdosgames.domain.GameIdentifier;
import de.bund.zrb.msdosgames.domain.GamePage;
import de.bund.zrb.msdosgames.domain.GameSearchCriteria;
import de.bund.zrb.msdosgames.domain.GameSummary;
import de.bund.zrb.msdosgames.domain.LicenseNotice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class InternetArchiveCatalogClient implements GameCatalog, GameDetailsProvider {

    private final HttpGateway httpGateway;
    private final Gson gson;
    private final InternetArchiveUrlBuilder urlBuilder;
    private final ArchiveSearchQueryBuilder searchQueryBuilder;
    private final ArchiveHtmlSanitizer htmlSanitizer;
    private final ArchiveFileFilter fileFilter;

    public InternetArchiveCatalogClient(HttpGateway httpGateway) {
        this(httpGateway, new Gson(), new InternetArchiveUrlBuilder(), new ArchiveSearchQueryBuilder(), new ArchiveHtmlSanitizer());
    }

    InternetArchiveCatalogClient(
            HttpGateway httpGateway,
            Gson gson,
            InternetArchiveUrlBuilder urlBuilder,
            ArchiveSearchQueryBuilder searchQueryBuilder,
            ArchiveHtmlSanitizer htmlSanitizer) {
        if (httpGateway == null) {
            throw new IllegalArgumentException("httpGateway must not be null");
        }
        this.httpGateway = httpGateway;
        this.gson = gson;
        this.urlBuilder = urlBuilder;
        this.searchQueryBuilder = searchQueryBuilder;
        this.htmlSanitizer = htmlSanitizer;
        this.fileFilter = new ArchiveFileFilter();
    }

    @Override
    public GamePage browse(GameSearchCriteria criteria) throws IOException {
        String query = searchQueryBuilder.buildBrowseQuery();
        return loadSearchPage(query, criteria);
    }

    @Override
    public GamePage search(GameSearchCriteria criteria) throws IOException {
        String query = searchQueryBuilder.buildSearchQuery(criteria.getQuery());
        return loadSearchPage(query, criteria);
    }

    @Override
    public GameDetails loadDetails(GameIdentifier identifier) throws IOException {
        String responseBody = httpGateway.getText(urlBuilder.buildMetadataUrl(identifier));
        ArchiveMetadataResponse response = gson.fromJson(responseBody, ArchiveMetadataResponse.class);
        ArchiveMetadataResponse.ArchiveMetadata metadata = response == null ? null : response.metadata;

        String title = metadata == null ? identifier.getValue() : metadata.title;
        String descriptionHtml = metadata == null ? "" : htmlSanitizer.sanitize(metadata.description);
        LicenseNotice licenseNotice = createLicenseNotice(identifier, metadata);
        List<GameFile> files = mapFiles(response == null ? Collections.<ArchiveMetadataResponse.ArchiveFile>emptyList() : response.files);
        long itemSize = response == null || response.item_size == null ? 0L : response.item_size.longValue();

        return new GameDetails(identifier, title, descriptionHtml, licenseNotice, files, itemSize);
    }

    private GamePage loadSearchPage(String query, GameSearchCriteria criteria) throws IOException {
        String responseBody = httpGateway.getText(urlBuilder.buildScrapeUrl(query, criteria));
        ArchiveSearchResponse response = gson.fromJson(responseBody, ArchiveSearchResponse.class);
        if (response == null || response.items == null) {
            return new GamePage(Collections.<GameSummary>emptyList(), null);
        }

        List<GameSummary> games = new ArrayList<GameSummary>();
        for (ArchiveSearchResponse.ArchiveSearchItem item : response.items) {
            if (item.identifier != null && item.identifier.trim().length() > 0) {
                games.add(mapSummary(item));
            }
        }
        return new GamePage(games, response.cursor);
    }

    private GameSummary mapSummary(ArchiveSearchResponse.ArchiveSearchItem item) {
        return new GameSummary(
                GameIdentifier.of(item.identifier),
                item.title,
                htmlSanitizer.toPlainText(item.description),
                item.creator,
                item.date,
                item.publicdate,
                item.downloads == null ? 0L : item.downloads.longValue(),
                item.item_size == null ? 0L : item.item_size.longValue());
    }

    private LicenseNotice createLicenseNotice(GameIdentifier identifier, ArchiveMetadataResponse.ArchiveMetadata metadata) {
        String licenseUrl = metadata == null ? "" : metadata.licenseurl;
        String rights = metadata == null ? "" : metadata.rights;
        return new LicenseNotice(licenseUrl, rights, urlBuilder.buildItemUrl(identifier));
    }

    private List<GameFile> mapFiles(List<ArchiveMetadataResponse.ArchiveFile> archiveFiles) {
        if (archiveFiles == null || archiveFiles.isEmpty()) {
            return Collections.emptyList();
        }

        List<GameFile> gameFiles = new ArrayList<GameFile>();
        for (ArchiveMetadataResponse.ArchiveFile archiveFile : archiveFiles) {
            if (fileFilter.accepts(archiveFile)) {
                gameFiles.add(new GameFile(
                        archiveFile.name,
                        archiveFile.format,
                        parseSize(archiveFile.size),
                        archiveFile.md5,
                        archiveFile.sha1));
            }
        }
        return gameFiles;
    }

    private long parseSize(String value) {
        if (value == null || value.trim().length() == 0) {
            return 0L;
        }
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException exception) {
            return 0L;
        }
    }
}
