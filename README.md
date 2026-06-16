# MS-DOS Game Browser

Java-8/Swing-Browser für die Archive.org-Collection `softwarelibrary_msdos_games`.

## Architektur

- `msdos-game-browser-domain`: reine Fachobjekte.
- `msdos-game-browser-application`: UseCases und Ports.
- `msdos-game-browser-infrastructure-archive`: Archive.org-API-Adapter und Download-Adapter.
- `msdos-game-browser-infrastructure-local`: lokale Speicherung für Lizenzannahmen und Verzeichnisse.
- `msdos-game-browser-frontend-swing`: Swing-UI ohne Archive.org-Abhängigkeit.
- `msdos-game-browser-launcher`: Verdrahtung der Adapter und Startklasse.

## Start

```bash
./gradlew build
./gradlew :msdos-game-browser-launcher:run
```

Unter Windows entsprechend:

```cmd
gradlew.bat build
gradlew.bat :msdos-game-browser-launcher:run
```

## Lizenzannahme

Downloads sind nur aktiv, nachdem die Lizenz- und Rechtehinweise des konkreten Archive.org-Items akzeptiert wurden. Die Annahme wird lokal pro Item und Lizenzsignatur gespeichert.
