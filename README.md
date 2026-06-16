# MS-DOS Game Browser

Java-8/Swing-Browser für die Archive.org-Collection `softwarelibrary_msdos_games`.

## Veröffentlichbare Artefakte

- `msdos-game-browser-backend`: öffentliche Backend-/API-Schicht für Suche, Details, Download, H2/Lucene-Cache, Favoriten und Archive.org-Hinweisannahmen.
- `msdos-game-browser-ui`: Swing-Anwendung auf Basis des Backend-Artefakts.

## Interne Architekturmodule

- `msdos-game-browser-domain`: Fachobjekte.
- `msdos-game-browser-application`: UseCases und Ports.
- `msdos-game-browser-infrastructure-archive`: Archive.org-API- und Download-Adapter.
- `msdos-game-browser-infrastructure-local`: lokale AppData-Verzeichnisse und JSON-Speicher.
- `msdos-game-browser-frontend-swing`: Swing-Komponenten ohne Archive.org-Direktverdrahtung.

## Preloading

Preloading ist im Backend standardmäßig ausgeschaltet. Dadurch wird nur der exklusive User-Loader erzeugt.

```java
MsdosGameBrowserBackend backend = MsdosGameBrowserBackend.createDefault();
```

Optional kann Preloading aktiviert werden:

```java
MsdosGameBrowserBackend backend = MsdosGameBrowserBackend.create(
    PreloadConfiguration.enabled(0, 12, 4, false)
);
```

Parameter:

- `lookBehind`: Anzahl Einträge vor dem aktuellen Bereich.
- `lookAhead`: Anzahl Einträge nach dem aktuellen Bereich.
- `detailThreads`: Anzahl Hintergrund-Threads für Detaildaten.
- `preloadImagesWhenIdle`: Bilder nur im Leerlauf vorladen.

## Start

```bash
./gradlew build
./gradlew :msdos-game-browser-ui:run
```

Unter Windows entsprechend:

```cmd
gradlew.bat build
gradlew.bat :msdos-game-browser-ui:run
```

## Lokale Daten

Unter Windows werden lokale Daten unter `%APPDATA%\.MS-DOS Game Browser` abgelegt:

- `favorites.json`
- `db/msdos-game-browser.mv.db`
- `settings.properties` für spätere Settings

Archive.org-Hinweisannahmen werden in der H2-Datenbank gespeichert. Favoriten bleiben eine getrennte JSON-Datei.