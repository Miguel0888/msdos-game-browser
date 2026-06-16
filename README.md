# MS-DOS Game Browser

Java-8-Backend und Swing-Oberfläche für die Archive.org-Collection `softwarelibrary_msdos_games`.

Der Schwerpunkt des Repositories ist die **Backend-API**: Suche, Browsing, Detaildaten, Archive.org-Hinweise, Downloads, Favoriten, H2/Lucene-Cache und optionales Preloading. Die Swing-UI ist ein separates Artefakt auf Basis dieser API.

> **Not affiliated with the Internet Archive.** This project uses public Archive.org
> search/metadata endpoints and item pages at runtime. **No games, WADs, ZIPs or other
> Archive.org content are bundled** in the published artifacts. Users are responsible for
> complying with the Internet Archive [Terms of Use](https://archive.org/about/terms.php)
> and with the access notices shown for individual items. Downloads only happen after an
> explicit user action and after the item's Archive.org notice has been accepted.

## Artefakte

Das Projekt ist auf zwei Maven-Central-Artefakte ausgelegt:

```text
msdos-game-browser-backend
msdos-game-browser-ui
```

### `msdos-game-browser-backend`

Für Anwendungen, die Archive.org-MS-DOS-Spiele selbst browsen, indexieren, cachen oder herunterladen wollen.

Enthält:

- öffentliche Backend-Fassade `MsdosGameBrowserBackend`
- `GameBrowserBackendService` für Browse/Search/Details/Image/Cache
- `DownloadGameUseCase`
- `AcceptArchiveNoticeUseCase`
- `FavoriteGamesUseCase`
- H2-Dateidatenbank unter AppData
- Lucene-Index im Backend
- Archive.org-API- und HTML-Parser
- optionales Preloading

### `msdos-game-browser-ui`

Swing-Anwendung, die das Backend-Artefakt verwendet.

Startklasse:

```text
com.aresstack.msdosgames.ui.MsdosGameBrowserApplication
```

## Backend Quickstart

```java
import com.aresstack.msdosgames.application.port.GameBrowserBackendService;
import com.aresstack.msdosgames.backend.MsdosGameBrowserBackend;
import com.aresstack.msdosgames.domain.GameDetails;
import com.aresstack.msdosgames.domain.GamePage;
import com.aresstack.msdosgames.domain.GameSearchCriteria;
import com.aresstack.msdosgames.domain.GameSummary;

public final class Example {
    public static void main(String[] args) throws Exception {
        MsdosGameBrowserBackend backend = MsdosGameBrowserBackend.createDefault();
        try {
            GameBrowserBackendService service = backend.getBrowserService();

            GamePage page = service.search(GameSearchCriteria.searchFirstPage("doom", 100));
            GameSummary first = page.getGames().get(0);

            GameDetails details = service.loadDetailsNow(first.getIdentifier());
            System.out.println(details.getTitle());
            System.out.println(details.getArchiveItemNotice().getAvailabilityText());
        } finally {
            backend.shutdown();
        }
    }
}
```

`createDefault()` erzeugt ein produktives Backend mit persistentem H2-Cache, Lucene-Index und lokalen AppData-Dateien. Preloading ist dabei absichtlich ausgeschaltet.

## Haupt-API

Die wichtigste API ist `GameBrowserBackendService`:

```java
GamePage browse(GameSearchCriteria criteria) throws IOException;
GamePage search(GameSearchCriteria criteria) throws IOException;
GameDetails loadDetailsNow(GameIdentifier identifier) throws Exception;
void preloadDetails(List<GameSummary> games, int firstIndex, int lastIndex);
byte[] loadPreviewImageNow(GameImage image) throws Exception;
void cancelLowPriorityImagePreloading();
void clearDatabaseCache() throws Exception;
void shutdown();
```

### Browsing und Suche

```java
GamePage firstPage = service.browse(GameSearchCriteria.browseFirstPage(100));
GamePage searchPage = service.search(GameSearchCriteria.searchFirstPage("duke nukem", 100));
```

Pagination läuft über den Cursor aus `GamePage`:

```java
if (searchPage.hasNextPage()) {
    GamePage next = service.search(
        GameSearchCriteria.searchNextPage("duke nukem", searchPage.getNextCursor(), 100)
    );
}
```

Der Archive.org-Such-Endpunkt (Scraping API) verlangt mindestens 100 Einträge pro Seite. Deshalb erzwingt `GameSearchCriteria` eine Java-8-kompatible, sichere Mindestgröße.

### Details laden

```java
GameDetails details = service.loadDetailsNow(summary.getIdentifier());
```

`loadDetailsNow` läuft über den exklusiven User-Loader. Er ist unabhängig vom optionalen Preloading und bleibt immer aktiv.

`GameDetails` enthält unter anderem:

- Titel
- Plain-Text-Beschreibung
- Download-Dateien, heuristisch sortiert
- Preview-Bilder als URLs
- Archive.org-Hinweise über `ArchiveItemNotice`
- erkannte Archive.org-Metadaten

### Archive.org-Hinweise

Archive.org liefert in dieser Collection nicht zuverlässig klassische Lizenzdaten. Das Backend modelliert deshalb keine künstliche Lizenz, sondern `ArchiveItemNotice`.

Erkannt werden zum Beispiel:

- `Download Options`
- `Streaming Only`
- `Free Borrow & Streaming`
- Terms-Link, falls auf der Seite vorhanden
- Item-Metadaten aus der Archive.org-HTML-Seite

Beispiel:

```java
String availability = details.getArchiveItemNotice().getAvailabilityText();
String sourceUrl = details.getArchiveItemNotice().getSourceUrl();
String accessText = details.getArchiveItemNotice().getAccessText();
```

## Downloads und Hinweisannahme

Downloads sind API-seitig bewusst UI-frei. Eine Anwendung muss vor einem Download sicherstellen, dass der Nutzer die Archive.org-Hinweise bestätigt hat.

```java
if (!backend.getAcceptArchiveNoticeUseCase().isAccepted(details)) {
    // UI oder CLI fragt hier den Nutzer.
    backend.getAcceptArchiveNoticeUseCase().accept(details);
}

backend.getDownloadGameUseCase().downloadAcceptedGame(
    details.getIdentifier(),
    details.getArchiveItemNotice(),
    details.getDownloadableFiles().get(0),
    backend.getDownloadDirectory(),
    progress -> System.out.println(progress.getPercent() + "%")
);
```

Die Annahme wird in der H2-Datenbank gespeichert. Beim nächsten Download desselben Items mit derselben Hinweis-Signatur wird nicht erneut gefragt.

Wenn die Annahme fehlt, wirft `DownloadGameUseCase` eine `ArchiveNoticeNotAcceptedException`.

## Favoriten

Favoriten sind getrennt vom Datenbank-Cache gespeichert, damit ein Cache-Löschen keine Bookmarks entfernt.

```java
backend.getFavoriteGamesUseCase().addFavorite(summary, details);
backend.getFavoriteGamesUseCase().listFavorites();
backend.getFavoriteGamesUseCase().removeFavorite(details.getIdentifier());
```

Speicherort:

```text
%APPDATA%\.MS-DOS Game Browser\favorites.json
```

## Preloading

Preloading ist **standardmäßig ausgeschaltet**.

```java
MsdosGameBrowserBackend backend = MsdosGameBrowserBackend.createDefault();
```

Damit werden nur die Ressourcen für direkte User-Aktionen erzeugt:

```text
exklusiver User-Loader: aktiv
Detail-Preload-Threadpool: aus
Image-Preload-Thread: aus
```

Preloading kann explizit aktiviert werden:

```java
import com.aresstack.msdosgames.backend.PreloadConfiguration;

MsdosGameBrowserBackend backend = MsdosGameBrowserBackend.create(
    PreloadConfiguration.enabled(
        0,      // lookBehind
        12,     // lookAhead
        4,      // detailThreads
        false   // preloadImagesWhenIdle
    )
);
```

Parameter:

| Parameter | Bedeutung |
|---|---|
| `lookBehind` | zusätzliche Einträge vor dem sichtbaren/aktuellen Bereich |
| `lookAhead` | zusätzliche Einträge nach dem sichtbaren/aktuellen Bereich |
| `detailThreads` | Hintergrund-Threads für Detaildaten |
| `preloadImagesWhenIdle` | Bilder nur im Leerlauf vorladen |

Das Backend stellt dafür bereit:

```java
service.preloadDetails(games, firstIndex, lastIndex);
```

Wenn Preloading deaktiviert ist, ist diese Methode ein No-op.

## Lokale Daten

Unter Windows nutzt das Backend:

```text
%APPDATA%\.MS-DOS Game Browser\
├─ favorites.json
├─ settings.properties
└─ db\
   └─ msdos-game-browser.mv.db
```

In der H2-Datenbank liegen aktuell:

- gecachte Detaildaten
- gecachte Preview-Bilder
- Archive.org-Hinweisannahmen

Der Datenbank-Cache kann über die API gelöscht werden:

```java
service.clearDatabaseCache();
```

Favoriten bleiben dabei erhalten, weil sie in `favorites.json` gespeichert werden.

## Swing-UI starten

```bash
./gradlew :msdos-game-browser-ui:run
```

Windows:

```cmd
gradlew.bat :msdos-game-browser-ui:run
```

## Build

```bash
./gradlew build
```

## Modulstruktur

```text
msdos-game-browser-domain
msdos-game-browser-application
msdos-game-browser-infrastructure-archive
msdos-game-browser-infrastructure-local
msdos-game-browser-backend
msdos-game-browser-frontend-swing
msdos-game-browser-ui
```

Die unteren Module halten die Architektur sauber. Veröffentlicht werden sollen primär:

```text
msdos-game-browser-backend
msdos-game-browser-ui
```

## Java-Version

Das Projekt zielt auf Java 8. Deshalb verwendet das Backend Lucene 8.11.x, weil neuere Lucene-Hauptversionen nicht mehr Java-8-kompatibel sind.
