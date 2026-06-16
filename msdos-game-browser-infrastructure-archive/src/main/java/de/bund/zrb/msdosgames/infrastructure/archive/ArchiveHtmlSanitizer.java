package de.bund.zrb.msdosgames.infrastructure.archive;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public final class ArchiveHtmlSanitizer {

    private final Safelist safelist;

    public ArchiveHtmlSanitizer() {
        this.safelist = Safelist.basic()
                .addTags("p", "br", "ul", "ol", "li", "pre", "code")
                .addAttributes("a", "href", "title")
                .addProtocols("a", "href", "http", "https");
    }

    public String sanitize(String html) {
        if (html == null || html.trim().length() == 0) {
            return "<html><body><p>Keine Beschreibung vorhanden.</p></body></html>";
        }
        return "<html><body>" + Jsoup.clean(html, safelist) + "</body></html>";
    }

    public String toPlainText(String html) {
        if (html == null || html.trim().length() == 0) {
            return "";
        }
        return Jsoup.parse(html).text();
    }
}
