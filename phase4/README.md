# DBS Programmierpraktikum – Phase 4 – Spring-Template

## Anleitung

Dies ist das auf Spring Boot beruhende Template für die API, die in der Phase 4 des DBS Programmierpraktikums nach
vorgegebener Spezifikation implementiert werden soll.

### Vorbereitung

Zuerst muss im Projektverzeichnis eine Datei mit Namen `gradle.properties` und folgendem Inhalt angelegt werden:

```properties
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.jvmargs=--add-exports jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED --add-exports jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED --add-exports jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED --add-exports jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED --add-exports jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED --add-exports jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED
de.hhu.git.pat=<PAT>
de.hhu.cs.dbs.propra.filename=<ZIP-NAME>

```

Dabei ist `<PAT>` ein in der GitLab-Instanz der HHU eingerichteter Personal Access Token mit (mindestens) `read_api`
-Scope und `<ZIP-NAME>` ist der ggf. laut Aufgabenstellung geforderte Name des ZIP-Archivs.

Danach sollte dieses Template als Gradle-Projekt in einer IDE importiert werden.

Um alle Tasks ordentlich ausführen zu können, müssen die entsprechenden Technologien (siehe [Links](#nützliche-links))
installiert sein.

> 🚨 Eine lokale **Installation von Docker Compose** ist zwingend notwendig.

Die Datenbank muss im Projektverzeichnis liegen und den Namen `database.db` besitzen.

### Allgemein

Die Mainklasse ist [`Application`](src/main/java/de/hhu/cs/dbs/propra/Application.java).
Nachdem das System mit bspw.

```shell
./gradlew :runContainerized
```

gestartet wurde, kann mit [cURL](#nützliche-links) oder [Swagger UI](#nützliche-links) (per http://localhost:8090) die
API getestet werden.

Das System kann mit

```shell
./gradlew :composeDown
```

gestoppt werden.

Alle Endpunkte der bereitgestellten OpenAPI-Spezifikation müssen im
Package [`de.hhu.cs.dbs.propra.presentation.rest`](src/main/java/de/hhu/cs/dbs/propra/presentation/rest) hinzugefügt
werden. Die darin enthaltene Klasse [**`ExampleController`**](src/main/java/de/hhu/cs/dbs/propra/presentation/rest/ExampleController.java) dient als Beispiel dafür.

Ob in den Controllern selbst mit der Datenbank kommuniziert wird oder dafür im
Package [`de.hhu.cs.dbs.propra.persistence.sql.sqlite`](src/main/java/de/hhu/cs/dbs/propra/persistence/sql/sqlite)
entsprechende Repositories angelegt werden, kann ausgesucht werden. Vergessen Sie nicht eine korrekte Authentifizierung
und Autorisierung zu implementieren, indem Sie die
Interfaces [`UserRepository`](src/main/java/de/hhu/cs/dbs/propra/model/UserRepository.java)
und [`RoleRepository`](src/main/java/de/hhu/cs/dbs/propra/model/RoleRepository.java) des
Package [`de.hhu.cs.dbs.propra.model`](src/main/java/de/hhu/cs/dbs/propra/model) im
Package [`de.hhu.cs.dbs.propra.persistence.sql.sqlite`](src/main/java/de/hhu/cs/dbs/propra/persistence/sql/sqlite) als
Spring Bean umsetzen.

> 🚨 Die Anforderungen der Aufgabenstellung müssen eingehalten werden.

Für die Abgabe kann

```shell
./gradlew :zipSubmission
```

im Projektverzeichnis ausgeführt werden, sodass die geforderten Dateien in einem ZIP-Archiv mit entsprechendem Namen (vgl. `gradle.properties`) im Ordner `build/distributions` hinterlegt werden.

### Wichtig

Sollte sich etwas an der API-Spezifikation ändern, so können Sie mit

```shell
./gradlew :updateSpecification
```

Ihre lokale Kopie aktualisieren. Diese wird zudem alle 30 Sekunden invalidiert, sodass sie automatisch beim nächsten
Start der Anwendung per Gradle neu geladen wird.

Wenn es Probleme beim Invalidieren gibt, so können Sie mit

```shell
./gradlew --refresh-dependencies
```

die Abhängigkeiten manuell neu laden.

### Nützliche Links

- [Adoptium](https://adoptium.net/de/)
- [cURL](https://curl.haxx.se)
- [Docker](https://www.docker.com)
- [Gradle](https://gradle.org)
- [Spring](https://spring.io)
- [Swagger](https://swagger.io)

## Kritische Entscheidungen


## Anleitung

Dies ist das auf Spring beruhende Template für die API.

### Vorbereitung

In der IDE muss dieses Template als Gradle-Projekt importiert werden.

### Allgemein

Die Mainklasse ist ```de.hhu.cs.dbs.propra.Application```.
Nachdem das Programm gestartet wurde, kann mit cURL der Server getestet werden.

Die Datenbank muss in ```data``` liegen und den Namen ```database.db``` besitzen.

Änderungen müssen hauptsächlich nur im Package ```de.hhu.cs.dbs.propra.presentation.rest``` vorgenommen werden.
Dies umfasst auch das Anlegen von Controllern.
Die darin enthaltene Klasse ```ExampleController``` dient als Beispiel dafür und muss für die Abgabe gelöscht werden.
Zusätzlich musste in der Klasse ```de/hhu/cs/dbs/propra/persistence/inmemory/InMemoryConfiguration``` entsprechend angepasst werden, um eine korrekte Authentifizierung und Authorisierung zu ermöglichen.

### Nützliche Links

- http://jdk.java.net [OpenJDK], https://adoptopenjdk.net [AdoptOpenJDK]
- https://gradle.org [Gradle]
- https://www.docker.com [Docker]
- https://www.spring.io [Spring]
- https://curl.haxx.se [cURL]

## Kritische Entscheidungen

### Als Künstler POST /song
Als Künstler wenn ich POST /song ausführe, bekamm man folgenden Error obwohl der Song erfolgreich hinzugefügt wurde.
Ich versuche den Song am Ende der insertSong Service Methode zu returnen, jedoch bekomme ich ein Optional.empty() raus, obwohl er den neu hinzugefügten Song finden sollte.
Die Sql Anfrage ist richtig, sie wurde auch in der Datenbank getestet und findet so den neuen Song.
Selbst wenn ich im Debugger bin und durch meine SongRepo die Sql Anfrage anschaue, kann er den neuen Song finden und trotzdem spuckt er mir am Ende Optional.Empty() raus...
Es muss sich hierbei um einen jdbc Fehler handeln.

Code	Details
400
Error: response status is 400

Response body
Download
{
"message": "Something went wrong"
}

Lösung: Tabellen der Datenbank löschen und wiederherstellen. Anschließend die Data.sql ausführen und nochmal POST /song durchführen.


### RestConfiguration.class fehlendes PATCH in der allowedMethods.
Als Nutzer um PATCH alben/{albumid}/kritiken/{kritikid} auszuführen, muss die Option PATCH in der Klasse RestConfiguration nochmal hinzugefügt werden, sonst bekommt man ein
Reject: HTTP 'PATCH' is not allowed. 403 FORBIDDEN


### Ausführen der Anwendung

- JDK 19.0.2
- Gradle Version 8.3
- Gradle JVM 19.0.2
- Command `./gradlew run` (unter Linux kann `./gradlewlinux run` verwendet werden)

