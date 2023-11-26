# Teil 1: ER-Modellierung der Datenbank

## Fragen während der Bearbeitung
- Premium-Variante oder den kostenlosen Dienst nutzen: Ist es sinnvoll es als Attribut "PremiumNutzer-Typ" zu setzen?
- Gibt es zwei Beziehungen: Künstler publiziert Aufnahme und Musiker produziert Song? Aus der Aufgabe geht dies hervor, aber Musiker ist eigentlich ein Künstler
- Kann ein Album auch von mindestens zwei Musiker veröffentlicht werden? Wenn zb Eminem und Rihanna ein Song rausbringen
- Kann ein Album von einer Band und mit Musiker veröffentlicht werden?
- Ist das Attribut Pseudonym ein Attribut vom Künstler oder ein Beziehungsattribut? es wird gesagt "wird veröffentlicht UNTER EINEM Pseudonym"
- Braucht die Entität Podcast eine weitere Beziehung mit dem Themenbereich? Es wird erwähnt "mindestens ein Themenbereich" ob das jetzt als Attribut von Podcast oder als eigene Entität behandelt werden soll?
- "Podcaster arbeiten professionell": Ist es ein Attribut?


## Kritische Entscheidungen

Im folgenden werden alle Entscheidungen bezüglich der Modellierung des ER-Modells begründet:


### Entitäten
--Es ist möglich zu einem Album mehrfach Kritik abzugeben. Man muss hier die Kritik als separate Entität auslagern in der Form: 
 "Nutzer" - [0,*] - "gibt" - [1,1] - "Kritik" - [1,1] - "gehört zu" - [0,*] - "Album" und Beziehung bewertet bekommt den Attribut Bewertung.
-- Es ist keine Mehrfachbewertung nötig, deshalb wird die Bewertung nicht als seperate Entität ausgelagert und ist dann in der Form:
 "Nutzer" - [0,*] - "bewertet" - [0,*]  - "Song"
-- Die im Anwendungsszenario erwähnten "Songs werden von mindestens einem oder in Zusammenarbeit mehrerer Musiker veröffentlicht." werden bereits durch die Relation publiziert mit den Entitäten Künstler und Aufnahme abgedeckt, da schon am Anfang "Künstler veröffentlicht Aufnahme" steht und Song ist eine Aufnahme.
    Man spart damit eine Tabelle und im ER Modell sieht man zwar das nicht, aber später in der Implementierung sieht man dann wer was veröffentlichen darf. Der Hintergrund dieser Entscheidung beruht auf die Vermeidung von Datenredundanz.
-- Entity Playlist:
 - Im ER-Modell kann man nicht sicherstellen, dass eine Playlist von genau einem Premium-Nutzer erstellt wird. Das kommt dann späteren Verlauf dazu.
-- Entity Nutzer:
 - Attribut Benutzername ist Primärschlüssel, weil es eindeutig ist und es keinen Sinn macht dass jemand anderes auch diesen Benutzernamen trägt.
 - Erstmal war die Idee zusätzlich noch ein Attribut PremiumNutzer-Typ zu setzen, ob der Nutzer jetzt den kostenlosen Dienst oder die Premium-Variante benutzt. Dann müsste man auch diesen Attribut auch bei Entität Premium-Nutzer setzen.
   Aber das ist schon durch die IST Beziehung alles abgedeckt, von daher braucht man das doch nicht.
-- Entity Künstler:
 - Attribut Pseudonym ist Primärschlüssel, weil in diesem Fall es kein Sinn macht wenn ein Künstler mehrere Pseudonyme hat und es ist eine Eigenschaft vom Künstler und es unique ist.
-- Entity Podcaster:
 - Attribut Level bedeutet, ob der Podcaster professionell ist oder nicht.
-- Entity Thema:
 - Ein Thema kann von mehreren Podcasts benutzt werden. Da das Thema zb. Wirtschaft in mehreren Podcasts vorkommen kann, wird es als extra Entität aufgefasst, wobei sichergestellt wird, dass das Thema Wirtschaft in einem Podcast nur einmal vorkommt.
-- Entity Band:
 - Attribut Name ist Primärschlüssel, weil der Name eindeutig sein muss und es keine andere Band auf der Welt geben darf, die den selben Namen hat. Es gibt ja nicht zweimal den Namen Black Eyed Peas.
 - Attribut Bandgeschichte ist optional.

### Relationen
-- Relation _gibt_ mit teilnehmenden Entities: _Nutzer_ und _Kritik_ (1:N):
 - [0,*]: Ein Nutzer kann keine oder mehrere Kritiks geben.
 - [1,1]: Kritik wird genau von einem Nutzer abgegeben.
-- Relation _gehört zu_ mit teilnehmenden Entities: _Kritik_ und _Album_ (N:1):
 - [1,1]: Eine Kritik gehört genau zu einem Album.
 - [0,*]: Ein Album kann keine oder mehrere Kritiks haben.
-- Relation _bewertet_ mit teilnehmenden Entities: _Nutzer_ und _Song_ (N:M):
 - [0,*]: Ein Nutzer kann keine oder mehrere Songs bewerten.
 - [0,*]: Ein Song kann von keinem oder von mehreren Nutzer bewertet werden.
-- Relation _erstellt_ mit teilnehmenden Entities: _Premium-Nutzer_ und _Playlist_ (1:N):
 - [0,*]: Ein _Premium-Nutzer_ soll mehrere Playlists erstellen können oder keine.
 - [0,1]: Eine _Playlist_ kann von einem Premium-Nutzer erstellt werden oder nicht
-- Relation _enthält_ mit teilnehmenden Entities: _Playlist_ und _Aufnahme_ (N:M):
 - Da eine Playlist eine Zusammenstellung von Songs und Podcasts ist und ich eine Tabelle sparren wollte, habe ich direkt die Beziehung "Playlist enthält Song" weggelassen weil Song eine Aufnahme ist und es in diese Relation gepackt.
   Der Hintergrund dieser Entscheidung beruht auf die Vermeidung von Datenredundanz.
   Aus der Aufgabenstellung bin so vorgegangen, dass ich unter "Playlist enthält mindestens einen Song" den Satz "Playlist enthält mindestens eine Aufnahme" verstanden habe und so es uns in der Sprechstunde geraten wurde.
 - [1,*]: Eie Playlist enthält mindestens eine Aufnahme und kann auch mehrere Aufnahme enthalten.
 - [0,*]: Eine Aufnahme kann in einer Playlist sein muss es aber nicht.
-- Relation _publiziert_ mit teilnehmenden Entities: _Künstler_ und _Aufnahme_ (N:M):
 - [0,*]: Ein _Künstler_ kann mehrere Aufnahmen veröffentlichen oder auch keine.
 - [1,*]: Eine _Aufnahme_ wird von mehreren Künstler veröffentlicht, aber mindestens von einem.
-- Relation _bildet_ mit teilnehmenden Entities: _Podcast_ und _Podcast-Episode_ (1:N):
 - [0,*]: Ein Podcast hat mindestens eine und mehrere Episoden.
 - [1,1]: Eine Episode gehört genau zu einem Podcast.
-- Relation _behandelt_ mit teilnehmenden Entities: _Thema_ und _Podcast_ (N:M):
 - [0,*]: Ein Thema kann von keinem oder von beliebig vielen Podcasts behandelt werden.
 - [1,1]: Ein Podcast behandelt mindestens ein Thema oder kann auch mehrere Themen behandeln.
-- Relation _besteht aus_ mit teilnehmenden Entities: _Musiker_ und _Band_ (N:1):
 - [0,1]: Ein Musiker gehört maximal zu einer Band und kann nicht zu einer anderen Band gehören.
 - [2,*]: Eine Band besteht aus mehreren Musiker, aber mindestens von zwei.
-- Relation _ist in_ mit teilnehmenden Entities: _Podcast-Episode_ und _Sprache_ (N:1):
 - Die Sprache der Podcast-Episode wurde als eigenständige Entität aufgefasst, um die Wiederverwendbarkeit dieser zu ermöglichen. Dies erleichtert im späteren Verlauf das Filtern nach einer bestimmten Sprache. Man könnte Sprache auch als Attribut von Episode auffassen, so wäre
   es schon quasi verschmolzen aber als Entität kann man die Sachen besser wiederverwenden und so wird auch Datenredundanz vermieden.
 - [1,1]: Eine Episode ist genau in einer Sprache.
 - [0,*]: Eine Sprache kann in mehreren Episode enthalten sein oder in keiner.
-- Relation _veröffentlicht_ mit teilnehmenden Entities: _Musiker_ und _Album_ (1:N):
 - [0,*]: Ein Musiker kann kein oder mehrere Alben veröffentlichen.
 - [0,1]: Ein Album kann von keinem oder von einem Musiker veröffentlicht werden.
-- Relation _veröffentlicht_ mit teilnehmenden Entities: _Band_ und _Album_ (1:N):
 - [0,*]: Eine Band kann kein oder mehrere Alben veröffentlichen.
 - [0,1]: Ein Album kann von keiner oder von einer Band veröffentlicht werden.
-- Relation _Bestandteil von_ mit teilnehmenden Entities: _Song_ und _Album_ (1:N):
 - [0,*]: Ein Album hat mindestens ein Song und bis zu 30 Songs drinnen. Ein Album ohne Songs macht keinen Sinn.
 - [0,1]: Ein Song kann zu keinem Album gehören dann wäre es eine Single oder kann nur zu einem Album gehören.

"Album wird vom einem Musiker oder einer Band veröffentlicht" Dieses ODER kann nicht im ER-Modell dargestellt werden und muss in zwei Beziehungen gesplittet sein.
Die Beziehung könnte als ternäre Beziehung aufgefasst werden, nur würde die Einschränkung dazuführen, dass leere Felder in einem Tupel vorhanden wären und das soll vermieden werden.
Deshalb wird die ternäre Beziehung in zwei binäre Beziehungen umgewandelt.

