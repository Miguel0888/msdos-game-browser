package de.bund.zrb.msdosgames.infrastructure.archive;

import de.bund.zrb.msdosgames.domain.GameIdentifier;
import de.bund.zrb.msdosgames.domain.GameSearchCriteria;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public final class InternetArchiveUrlBuilder {

    private static final String SCRAPE_URL = "https://archive.org/services/search/v1/scrape";
    private static final String METADATA_URL = "https://archive.org/metadata/";
    private static final String DOWNLOAD_URL = "https://archive.org/download/";
    private static final String SEARCH_FIELDS = "identifier,title,creator,date,description,downloads,item_size,publicdate,licenseurl,rights";

    public String buildScrapeUrl(String query, GameSearchCriteria criteria) {
        StringBuilder url = new StringBuilder(SCRAPE_URL);
        url.append("?q=").append(encode(query));
        url.append("&fields=").append(encode(SEARCH_FIELDS));
        url.append("&sorts=").append(encode("titleSorter,identifier"));
        url.append("&count=").append(criteria.getPageSize());
        if (criteria.hasCursor()) {
            url.append("&cursor=").append(encode(criteria.getCursor()));
        }
        return url.toString();
    }

    public String buildMetadataUrl(GameIdentifier identifier) {
        return METADATA_URL + encodePathSegment(identifier.getValue());
    }

    public String buildDownloadUrl(GameIdentifier identifier, String fileName) {
        return DOWNLOAD_URL + encodePathSegment(identifier.getValue()) + "/" + encodePath(fileName);
    }

    public String buildItemUrl(GameIdentifier identifier) {
        return "https://archive.org/details/" + encodePathSegment(identifier.getValue());
    }

    private String encodePath(String path) {
        String[] segments = path.split("/");
        StringBuilder encodedPath = new StringBuilder();
        for (int index = 0; index < segments.length; index++) {
            if (index > 0) {
                encodedPath.append('/');
            }
            encodedPath.append(encodePathSegment(segments[index]));
        }
        return encodedPath.toString();
    }

    private String encodePathSegment(String value) {
        return encode(value).replace("+", "%20");
    }

    private String encode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException exception) {
            throw new IllegalStateException("UTF-8 must be available", exception);
        }
    }
}
