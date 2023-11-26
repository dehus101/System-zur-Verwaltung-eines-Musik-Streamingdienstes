/*
- Hat VARCHAR: 
    - nicht die leere Zeichenkette enthalten ✅
    - nur ASCII Zeichen im Bereich 20-7E enthalten ✅
- Nicht optionale Attribute: NOT NULL ✅
- Benutzername:
    Der Benutzername hat eine Länge von 7 Zeichen, wobei die ersten 5 Zeichen Buchstaben und die letzten beiden Zeichen Ziffern sind.
-IBAN:
    Es erfolgt keine spezifische Überprüfung der IBAN in der Datenbank.
    Die IBAN-Validierung sollte in einer Anwendungslogik außerhalb der Datenbank erfolgen.

- Email: Format X@Y.Z mit X, Y und Z nicht leer ✅
- Password: 
    - zwischen 5 und 9 Zeichen, ✅
    - mindestens 1 Großbuchstabe, ✅
    - mindestens 2 Ziffern, ✅
    - auf Vokale folgen keine Ziffern aus der Menge {Q,X,Y,Z} ✅

Eigentlich sollte diese Version auch funktionieren:
            AND (substr(Benutzername, 1, 5) NOT GLOB '*[A-Za-z]*')
            AND (substr(Benutzername, 6, 2) LIKE '[0-9][0-9]')
*/

CREATE TABLE IF NOT EXISTS Nutzer (
    Benutzername VARCHAR(7)
        COLLATE NOCASE 
        NOT NULL
        CHECK(
            (LENGTH(Benutzername) == 7)
            AND (Benutzername GLOB '[A-Za-z][A-Za-z][A-Za-z][A-Za-z][A-Za-z][0-9][0-9]')
        ),
    Email VARCHAR(256) 
        UNIQUE
        NOT NULL 
        COLLATE NOCASE 
        CHECK(
            (substr(Email,1, INSTR(Email,'@')-1) NOT GLOB '*[^A-Za-z0-9]*')
            AND (substr(Email, INSTR(Email,'@')+1, ((INSTR(Email,'.')-1) - (INSTR(Email,'@')+1))+1) NOT GLOB '*[^A-Za-z0-9]*') 
            AND (substr(Email, INSTR(Email,'.')+1) NOT GLOB '*[^A-Za-z]*') 
            AND Email LIKE '%_@%_.%_'
        ),
    IBAN VARCHAR(34)
        COLLATE NOCASE
        NOT NULL,
    Passwort VARCHAR(9) NOT NULL CHECK(
        (LENGTH(Passwort) BETWEEN 5 AND 9)
        AND (Passwort NOT GLOB '*[aeiouAEIOU][QXYZ]*')
        AND (Passwort GLOB '*[A-Z]*')
        AND (Passwort GLOB '*[0-9]*[0-9]*')
        AND (Passwort NOT GLOB '*[^ -~]*')
    ),
    PRIMARY KEY (Benutzername)
);

/*
- Hat VARCHAR: 
    - nicht die leere Zeichenkette enthalten ✅
    - nur ASCII Zeichen im Bereich 20-7E enthalten ✅
    - case-insensitive ✅
- Nicht optionale Attribute: NOT NULL ✅
- Modifikationen bei nicht-künstlichen Primärschlüsseln: 
    - ON DELETE CASCADE ✅
    - ON UPDATE CASCADE ✅
- Schlüsselkandidaten mit Datentyp VARCHAR Case-insensitive: COLLATE NOCASE ✅
- Alle Schlüsselkandidaten 
    - müssen mit UNIQUE gekennzeichnet werden ✅
*/

CREATE TABLE IF NOT EXISTS PremiumNutzer (
     Benutzername VARCHAR(256) 
        COLLATE NOCASE
        NOT NULL, 
    Gültigkeitsdauer DATETIME
        NOT NULL
        CHECK ( Gültigkeitsdauer IS DATETIME(Gültigkeitsdauer) ),
    PRIMARY KEY (Benutzername),
    FOREIGN KEY (Benutzername) REFERENCES Nutzer (Benutzername)
    ON UPDATE CASCADE 
    ON DELETE CASCADE
);

/*
- Hat VARCHAR: 
    - nicht die leere Zeichenkette enthalten ✅
    - nur ASCII Zeichen im Bereich 20-7E enthalten ✅
    - case-insensitive ✅
- Nicht optionale Attribute: NOT NULL ✅
- Modifikationen bei nicht-künstlichen Primärschlüsseln: 
    - ON DELETE CASCADE ✅
    - ON UPDATE CASCADE ✅
- Alle Schlüsselkandidaten 
    - müssen mit UNIQUE gekennzeichnet werden ✅
*/


CREATE TABLE IF NOT EXISTS Künstler (
     Pseudonym VARCHAR(256)
        COLLATE NOCASE
        NOT NULL, 
    Benutzername VARCHAR(256)
        NOT NULL,
    Aktivitätsdatum DATETIME
        NOT NULL
        CHECK ( Aktivitätsdatum IS DATETIME(Aktivitätsdatum) ),
    PRIMARY KEY (Pseudonym),
    FOREIGN KEY (Benutzername) REFERENCES PremiumNutzer (Benutzername)
    ON UPDATE CASCADE 
    ON DELETE CASCADE
);

/*
- Hat VARCHAR: 
    - nicht die leere Zeichenkette enthalten ✅
    - nur ASCII Zeichen im Bereich 20-7E enthalten ✅
    - case-insensitive ✅
- Nicht optionale Attribute: NOT NULL ✅
- Modifikationen bei nicht-künstlichen Primärschlüsseln: 
    - ON DELETE CASCADE ✅
    - ON UPDATE CASCADE ✅
-Level
 1: professionell
 0: nicht professionell
- Alle Schlüsselkandidaten 
    - müssen mit UNIQUE gekennzeichnet werden ✅
*/

CREATE TABLE IF NOT EXISTS Podcaster (
     Pseudonym VARCHAR(256)
        COLLATE NOCASE
        NOT NULL, 
    Level BOOLEAN
        NOT NULL,
    PRIMARY KEY (Pseudonym),
    FOREIGN KEY (Pseudonym) REFERENCES Künstler (Pseudonym)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

/*
- Hat VARCHAR: 
    - nicht die leere Zeichenkette enthalten ✅
    - nur ASCII Zeichen im Bereich 20-7E enthalten ✅
    - case-insensitive ✅
- Nicht optionale Attribute: NOT NULL ✅
- Modifikationen bei nicht-künstlichen Primärschlüsseln: 
    - ON DELETE CASCADE ✅
    - ON UPDATE CASCADE ✅
- Alle Schlüsselkandidaten 
    - müssen mit UNIQUE gekennzeichnet werden ✅
*/

CREATE TABLE IF NOT EXISTS Musiker (
     Pseudonym VARCHAR(256)
        COLLATE NOCASE
        NOT NULL,
    auftrittfähig BOOLEAN NOT NULL,
    PRIMARY KEY (Pseudonym),
    FOREIGN KEY (Pseudonym) REFERENCES Künstler (Pseudonym)
    ON UPDATE CASCADE 
    ON DELETE CASCADE
);

/*
- Hat VARCHAR: 
    - nicht die leere Zeichenkette enthalten ✅
    - nur ASCII Zeichen im Bereich 20-7E enthalten ✅
    - case-insensitive ✅
- Nicht optionale Attribute: NOT NULL ✅
- Modifikationen bei nicht-künstlichen Primärschlüsseln: 
    - ON DELETE CASCADE ✅
    - ON UPDATE CASCADE ✅
- Alle Schlüsselkandidaten 
    - müssen mit UNIQUE gekennzeichnet werden ✅
*/

CREATE TABLE IF NOT EXISTS Band (
     Name VARCHAR(256)
        COLLATE NOCASE
        NOT NULL, 
    Bandgeschichte TEXT
         CHECK (
            (Bandgeschichte NOT GLOB '*[^ -~]*')
            AND (LENGTH(Bandgeschichte)>0)
        ),
    PRIMARY KEY (Name)
);

/*
- Hat VARCHAR: 
    - nicht die leere Zeichenkette enthalten ✅
    - nur ASCII Zeichen im Bereich 20-7E enthalten ✅
    - case-insensitive ✅
- Nicht optionale Attribute: NOT NULL ✅
- Modifikationen bei nicht-künstlichen Primärschlüsseln: 
    - ON DELETE CASCADE ✅
    - ON UPDATE CASCADE ✅
- Alle Schlüsselkandidaten 
    - müssen mit UNIQUE gekennzeichnet werden ✅
*/

CREATE TABLE IF NOT EXISTS Playlist (
     ID INTEGER
        NOT NULL, 
    Name VARCHAR(256)
        COLLATE NOCASE
        NOT NULL,
    Sichtbarkeit BOOLEAN NOT NUll,
    PRIMARY KEY (ID)
    
);

/*
- Hat VARCHAR:
    - nicht die leere Zeichenkette enthalten ✅
    - nur ASCII Zeichen im Bereich 20-7E enthalten ✅
    - case-insensitive ✅
- Die Bezeichnung des Genre besteht nur aus Zeichen des lateinischen Alphabets. ✅
- Nicht optionale Attribute: NOT NULL ✅
*/

CREATE TABLE IF NOT EXISTS Genre (
    ID INTEGER NOT NULL, 
    Bezeichnung VARCHAR(256)
        COLLATE NOCASE
        NOT NULL
        CHECK(
            Bezeichnung GLOB '[A-Za-z]*'),
    PRIMARY KEY (ID)
);

/*
- Hat VARCHAR: 
    - nicht die leere Zeichenkette enthalten ✅
    - nur ASCII Zeichen im Bereich 20-7E enthalten ✅
    - case-insensitive ✅
Das Erscheinungsjahr hat das Format “YYYY”. ✅
- Nicht optionale Attribute: NOT NULL ✅
*/

CREATE TABLE IF NOT EXISTS Album (
    ID INTEGER NOT NULL, 
    Bezeichnung VARCHAR(256)
        COLLATE NOCASE
        NOT NULL,
    Erscheinungsjahr INT
        NOT NULL
        CHECK ( Erscheinungsjahr BETWEEN 1000 AND 9999 ),
    PRIMARY KEY (ID)
);

/*
- Hat VARCHAR: 
    - nicht die leere Zeichenkette enthalten ✅
    - nur ASCII Zeichen im Bereich 20-7E enthalten ✅
    - case-insensitive ✅
- Nicht optionale Attribute: NOT NULL ✅
*/

CREATE TABLE IF NOT EXISTS Song (
    ID INTEGER NOT NULL, 
    SpeicherortLQ VARCHAR(256) COLLATE NOCASE NOT NULL,
    SpeicherortHQ VARCHAR(256) COLLATE NOCASE NOT NULL,
    Genre INTEGER NOT NULL,
    PRIMARY KEY (ID),
    FOREIGN KEY (ID) REFERENCES Aufnahme (ID),
    FOREIGN KEY (Genre) REFERENCES Genre (ID)
    ON UPDATE CASCADE 
    ON DELETE CASCADE
);

/*
- Hat VARCHAR: 
    - nicht die leere Zeichenkette enthalten ✅
    - nur ASCII Zeichen im Bereich 20-7E enthalten ✅
    - case-insensitive ✅
- Nicht optionale Attribute: NOT NULL ✅
- Modifikationen bei nicht-künstlichen Primärschlüsseln: 
    - ON DELETE CASCADE ✅
    - ON UPDATE CASCADE ✅
- Alle Schlüsselkandidaten 
    - müssen mit UNIQUE gekennzeichnet werden ✅
*/



CREATE TABLE IF NOT EXISTS Kritik (
    ID INTEGER NOT NULL, 
    Text TEXT
        COLLATE NOCASE
        NOT NULL
         CHECK (
            (Text NOT GLOB '*[^ -~]*')
            AND (LENGTH(Text)>0)
        ),
    Benutzername VARCHAR(256)
        COLLATE NOCASE
        UNIQUE
        NOT NULL,
    Album INTEGER NOT NULL,
    PRIMARY KEY (ID),
    FOREIGN KEY (Benutzername) REFERENCES Nutzer (Benutzername),
    FOREIGN KEY (Album) REFERENCES Album (ID)
    ON UPDATE CASCADE 
    ON DELETE CASCADE
);

/*
- Hat VARCHAR: 
    - nicht die leere Zeichenkette enthalten ✅
    - nur ASCII Zeichen im Bereich 20-7E enthalten ✅
    - case-insensitive ✅
- Dauer:
    Integer weil in Sekunden
- Nicht optionale Attribute: NOT NULL ✅
- Modifikationen bei nicht-künstlichen Primärschlüsseln: 
    - ON DELETE CASCADE ✅
    - ON UPDATE CASCADE ✅
- Alle Schlüsselkandidaten 
    - müssen mit UNIQUE gekennzeichnet werden ✅
*/

CREATE TABLE IF NOT EXISTS Aufnahme (
     ID INTEGER
        NOT NULL, 
    Dauer INT NOT NULL,
    Titel VARCHAR(256) 
        COLLATE NOCASE
        NOT NULL, 
    PRIMARY KEY (ID)
    
);

/*
- Hat VARCHAR: 
    - nicht die leere Zeichenkette enthalten ✅
    - nur ASCII Zeichen im Bereich 20-7E enthalten ✅
    - case-insensitive ✅
- Nicht optionale Attribute: NOT NULL ✅
*/

CREATE TABLE IF NOT EXISTS Podcast (
    ID INTEGER NOT NULL, 
    Name VARCHAR(256)
        COLLATE NOCASE
        NOT NULL,
    PRIMARY KEY (ID)
);

/*
- Hat VARCHAR: 
    - nicht die leere Zeichenkette enthalten ✅
    - nur ASCII Zeichen im Bereich 20-7E enthalten ✅
    - case-insensitive ✅
- Nicht optionale Attribute: NOT NULL ✅
*/

CREATE TABLE IF NOT EXISTS Sprache (
    ID INTEGER NOT NULL, 
    Bezeichnung VARCHAR(256)
        COLLATE NOCASE
        NOT NULL,
    PRIMARY KEY (ID)
);

/*
- Hat VARCHAR: 
    - nicht die leere Zeichenkette enthalten ✅
    - nur ASCII Zeichen im Bereich 20-7E enthalten ✅
    - case-insensitive ✅
- Nicht optionale Attribute: NOT NULL ✅
*/

CREATE TABLE IF NOT EXISTS Thema (
    ID INTEGER NOT NULL, 
    Name VARCHAR(256) 
        COLLATE NOCASE
        NOT NULL,
    PRIMARY KEY (ID)
);


/*
- Hat VARCHAR: 
    - nicht die leere Zeichenkette enthalten ✅
    - nur ASCII Zeichen im Bereich 20-7E enthalten ✅
    - case-insensitive ✅
- Nicht optionale Attribute: NOT NULL ✅
- Modifikationen bei nicht-künstlichen Primärschlüsseln: 
    - ON DELETE CASCADE ✅
    - ON UPDATE CASCADE ✅
- Alle Schlüsselkandidaten 
    - müssen mit UNIQUE gekennzeichnet werden ✅
*/

CREATE TABLE IF NOT EXISTS PodcastEpisode (
    ID INTEGER
        NOT NULL, 
    Nummer INTEGER NOT NULL,
    Podcast INTEGER
        NOT NULL,
    Sprache INTEGER
        NOT NULL,
    PRIMARY KEY (ID),
    FOREIGN KEY (ID) REFERENCES Aufnahme (ID),

    FOREIGN KEY (Podcast) REFERENCES Podcast (ID),

    FOREIGN KEY (Sprache) REFERENCES Sprache (ID)
    ON UPDATE CASCADE 
    ON DELETE CASCADE
);

/*
- Hat VARCHAR:
    - nicht die leere Zeichenkette enthalten ✅
    - nur ASCII Zeichen im Bereich 20-7E enthalten ✅
    - case-insensitive ✅
- Nicht optionale Attribute: NOT NULL ✅
- Modifikationen bei nicht-künstlichen Primärschlüsseln:
    - ON DELETE CASCADE ✅
    - ON UPDATE CASCADE ✅
- Alle Schlüsselkandidaten
    - müssen mit UNIQUE gekennzeichnet werden ✅
*/



CREATE TABLE IF NOT EXISTS Nutzer_like_dislike_Playlist (
      Nutzer VARCHAR(256)
          COLLATE NOCASE
          NOT NULL,
      Playlist INTEGER
          NOT NULL,
      Wert VARCHAR(7)
          COLLATE NOCASE
          CHECK(Wert IN ('like', 'dislike')),
      PRIMARY KEY (Nutzer, Playlist),
      FOREIGN KEY (Nutzer) REFERENCES Nutzer (Benutzername),
      FOREIGN KEY (Playlist) REFERENCES Playlist (ID)
          ON UPDATE CASCADE
          ON DELETE CASCADE
);

/*
- Hat VARCHAR:
    - nicht die leere Zeichenkette enthalten ✅
    - nur ASCII Zeichen im Bereich 20-7E enthalten ✅
    - case-insensitive ✅
- Nicht optionale Attribute: NOT NULL ✅
- Modifikationen bei nicht-künstlichen Primärschlüsseln:
    - ON DELETE CASCADE ✅
    - ON UPDATE CASCADE ✅
Der Wertebereich der Bewertungsskala ist {1, … ,5}. ✅
Alle Schlüsselkandidaten
    - müssen mit UNIQUE gekennzeichnet werden ✅
Song referenziert hierbei eine Song ID. Würde man Aufnahme ID
referenzieren, so könnte ein Nutzer auch eine Aufnahme an
sich bzw. eine Podcast Episode bewerten.
*/



CREATE TABLE IF NOT EXISTS Nutzer_bewertet_Song (
    Nutzer VARCHAR(256)
        COLLATE NOCASE
        NOT NULL,
    Song INTEGER
        NOT NULL,
    Bewertung INTEGER
        NOT NULL
        CHECK (Bewertung BETWEEN 1 AND 5),
    PRIMARY KEY (Nutzer, Song),
    FOREIGN KEY (Nutzer) REFERENCES Nutzer (Benutzername),
    FOREIGN KEY (Song) REFERENCES Song (ID)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

/*
- Hat VARCHAR:
    - nicht die leere Zeichenkette enthalten ✅
    - nur ASCII Zeichen im Bereich 20-7E enthalten ✅
    - case-insensitive ✅
- Nicht optionale Attribute: NOT NULL ✅
- Modifikationen bei nicht-künstlichen Primärschlüsseln:
    - ON DELETE CASCADE ✅
    - ON UPDATE CASCADE ✅
Alle Schlüsselkandidaten
 - müssen mit UNIQUE gekennzeichnet werden ✅
*/

CREATE TABLE IF NOT EXISTS PremiumNutzer_erstellt_Playlist (
    PremiumNutzer VARCHAR(256)
        COLLATE NOCASE
        NOT NULL,
    Playlist INTEGER
        NOT NULL,
    PRIMARY KEY (Playlist),
    FOREIGN KEY (Playlist) REFERENCES Playlist (ID),
    FOREIGN KEY (PremiumNutzer) REFERENCES PremiumNutzer (Benutzername)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

/*
- Hat VARCHAR:
    - nicht die leere Zeichenkette enthalten ✅
    - nur ASCII Zeichen im Bereich 20-7E enthalten ✅
    - case-insensitive ✅
- Nicht optionale Attribute: NOT NULL ✅
- Modifikationen bei nicht-künstlichen Primärschlüsseln:
    - ON DELETE CASCADE ✅
    - ON UPDATE CASCADE ✅
Alle Schlüsselkandidaten
 - müssen mit UNIQUE gekennzeichnet werden ✅
*/

CREATE TABLE IF NOT EXISTS Playlist_enthält_Aufnahme (
   Playlist INTEGER
       NOT NULL,
   Aufnahme INTEGER
       NOT NULL,
   PRIMARY KEY (Playlist, Aufnahme),
   FOREIGN KEY (Playlist) REFERENCES Playlist (ID),
   FOREIGN KEY (Aufnahme) REFERENCES Aufnahme (ID)
       ON UPDATE CASCADE
       ON DELETE CASCADE
);

/*
- Hat VARCHAR:
    - nicht die leere Zeichenkette enthalten ✅
    - nur ASCII Zeichen im Bereich 20-7E enthalten ✅
    - case-insensitive ✅
- Nicht optionale Attribute: NOT NULL ✅
- Modifikationen bei nicht-künstlichen Primärschlüsseln:
    - ON DELETE CASCADE ✅
    - ON UPDATE CASCADE ✅
Alle Schlüsselkandidaten
 - müssen mit UNIQUE gekennzeichnet werden ✅
*/

CREATE TABLE IF NOT EXISTS Podcast_behandelt_Thema (
   Podcast INTEGER
       NOT NULL,
   Thema INTEGER
       NOT NULL,
   PRIMARY KEY (Podcast, Thema),
   FOREIGN KEY (Podcast) REFERENCES Podcast (ID),
   FOREIGN KEY (Thema) REFERENCES Thema (ID)
       ON UPDATE CASCADE
       ON DELETE CASCADE
);

/*
- Hat VARCHAR:
    - nicht die leere Zeichenkette enthalten ✅
    - nur ASCII Zeichen im Bereich 20-7E enthalten ✅
    - case-insensitive ✅
- Nicht optionale Attribute: NOT NULL ✅
- Modifikationen bei nicht-künstlichen Primärschlüsseln:
    - ON DELETE CASCADE ✅
    - ON UPDATE CASCADE ✅
Alle Schlüsselkandidaten
 - müssen mit UNIQUE gekennzeichnet werden ✅
*/

CREATE TABLE IF NOT EXISTS Künstler_publiziert_Aufnahme (
   Künstler VARCHAR(256)
       COLLATE NOCASE
       NOT NULL,
   Aufnahme INTEGER
       NOT NULL,
   PRIMARY KEY (Künstler, Aufnahme),
   FOREIGN KEY (Künstler) REFERENCES Künstler (Pseudonym),
   FOREIGN KEY (Aufnahme) REFERENCES Aufnahme (ID)
       ON UPDATE CASCADE
       ON DELETE CASCADE
);

/*
- Hat VARCHAR:
    - nicht die leere Zeichenkette enthalten ✅
    - nur ASCII Zeichen im Bereich 20-7E enthalten ✅
    - case-insensitive ✅
- Nicht optionale Attribute: NOT NULL ✅
- Modifikationen bei nicht-künstlichen Primärschlüsseln:
    - ON DELETE CASCADE ✅
    - ON UPDATE CASCADE ✅
Alle Schlüsselkandidaten
 - müssen mit UNIQUE gekennzeichnet werden ✅
*/

CREATE TABLE IF NOT EXISTS Podcaster_arbeitet_mit (
   Podcaster1 VARCHAR(256)
       COLLATE NOCASE
       NOT NULL,
   Podcaster2 VARCHAR(256)
       COLLATE NOCASE
       NOT NULL,
   PRIMARY KEY (Podcaster1, Podcaster2),
   FOREIGN KEY (Podcaster1) REFERENCES Podcaster (Pseudonym),
   FOREIGN KEY (Podcaster2) REFERENCES Podcaster (Pseudonym)
       ON UPDATE CASCADE
       ON DELETE CASCADE
);

/*
- Hat VARCHAR:
    - nicht die leere Zeichenkette enthalten ✅
    - nur ASCII Zeichen im Bereich 20-7E enthalten ✅
    - case-insensitive ✅
- Nicht optionale Attribute: NOT NULL ✅
- Modifikationen bei nicht-künstlichen Primärschlüsseln:
    - ON DELETE CASCADE ✅
    - ON UPDATE CASCADE ✅
Alle Schlüsselkandidaten
 - müssen mit UNIQUE gekennzeichnet werden ✅
*/

CREATE TABLE IF NOT EXISTS Band_besteht_aus_Musiker (
      Band VARCHAR(265)
          COLLATE NOCASE
          NOT NULL,
      Musiker VARCHAR(256)
          COLLATE NOCASE
          NOT NULL,
      PRIMARY KEY (Musiker),
      FOREIGN KEY (Band) REFERENCES Band (Name),
      FOREIGN KEY (Musiker) REFERENCES Musiker (Pseudonym)
          ON UPDATE CASCADE
          ON DELETE CASCADE
);

/*
- Hat VARCHAR:
    - nicht die leere Zeichenkette enthalten ✅
    - nur ASCII Zeichen im Bereich 20-7E enthalten ✅
    - case-insensitive ✅
- Nicht optionale Attribute: NOT NULL ✅
- Modifikationen bei nicht-künstlichen Primärschlüsseln:
    - ON DELETE CASCADE ✅
    - ON UPDATE CASCADE ✅
Alle Schlüsselkandidaten
 - müssen mit UNIQUE gekennzeichnet werden ✅
*/

CREATE TABLE IF NOT EXISTS Song_Bestandteil_von_Album (
      Song INTEGER
          NOT NULL,
      Album INTEGER
          NOT NULL,
      PRIMARY KEY (Song),
      FOREIGN KEY (Song) REFERENCES Song (ID),
      FOREIGN KEY (Album) REFERENCES Album (ID)
          ON UPDATE CASCADE
          ON DELETE CASCADE
);

/*
- Hat VARCHAR:
    - nicht die leere Zeichenkette enthalten ✅
    - nur ASCII Zeichen im Bereich 20-7E enthalten ✅
    - case-insensitive ✅
- Nicht optionale Attribute: NOT NULL ✅
- Modifikationen bei nicht-künstlichen Primärschlüsseln:
    - ON DELETE CASCADE ✅
    - ON UPDATE CASCADE ✅
Alle Schlüsselkandidaten
 - müssen mit UNIQUE gekennzeichnet werden ✅
*/

CREATE TABLE IF NOT EXISTS Musiker_veröffentlicht_Album (
      Musiker VARCHAR(256)
          COLLATE NOCASE
          NOT NULL,
      Album INTEGER
          NOT NULL,
      PRIMARY KEY (Album),
      FOREIGN KEY (Musiker) REFERENCES Musiker (Pseudonym),
      FOREIGN KEY (Album) REFERENCES Album (ID)
          ON UPDATE CASCADE
          ON DELETE CASCADE
);

/*
- Hat VARCHAR:
    - nicht die leere Zeichenkette enthalten ✅
    - nur ASCII Zeichen im Bereich 20-7E enthalten ✅
    - case-insensitive ✅
- Nicht optionale Attribute: NOT NULL ✅
- Modifikationen bei nicht-künstlichen Primärschlüsseln:
    - ON DELETE CASCADE ✅
    - ON UPDATE CASCADE ✅
Alle Schlüsselkandidaten
 - müssen mit UNIQUE gekennzeichnet werden ✅
*/

CREATE TABLE IF NOT EXISTS Band_veröffentlicht_Album (
      Band VARCHAR(256)
          COLLATE NOCASE
          NOT NULL,
      Album INTEGER
          NOT NULL,
      PRIMARY KEY (Album),
      FOREIGN KEY (Album) REFERENCES Album (ID),
      FOREIGN KEY (Band) REFERENCES Band (Name)
          ON UPDATE CASCADE
          ON DELETE CASCADE
);

CREATE TRIGGER tr_check_album_musiker_before_insert
    BEFORE INSERT ON Musiker_veröffentlicht_Album
    FOR EACH ROW
BEGIN
    SELECT RAISE(ABORT, 'Dieses Album wurde bereits von einer Band veröffentlicht!')
    WHERE EXISTS (
                  SELECT 1 FROM Band_veröffentlicht_Album WHERE Album = NEW.Album
              );
END;

CREATE TRIGGER tr_check_album_band_bevor_einfuegen
    BEFORE INSERT ON Band_veröffentlicht_Album
    FOR EACH ROW
BEGIN
    SELECT RAISE(ABORT, 'Dieses Album wurde bereits von einem Musiker veröffentlicht!')
    WHERE EXISTS (
                  SELECT 1 FROM Musiker_veröffentlicht_Album WHERE Album = NEW.Album
              );
END;

CREATE TRIGGER tr_check_song_bevor_bewerten
    BEFORE INSERT ON Nutzer_bewertet_Song
    FOR EACH ROW
BEGIN
    SELECT RAISE(ABORT, 'Songs, die Bestandteil eines Albums sind, können nicht bewertet werden!')
    WHERE EXISTS (
                  SELECT 1 FROM Song_Bestandteil_von_Album WHERE Song = NEW.Song
              );
END;

CREATE TRIGGER tr_check_podcaster_zusammenarbeit_bevor_einfuegen
    BEFORE INSERT ON Podcaster_arbeitet_mit
    FOR EACH ROW
BEGIN
    SELECT RAISE(ABORT, 'Ein Podcaster kann nicht mit sich selbst zusammenarbeiten!')
    WHERE NEW.Podcaster1 = NEW.Podcaster2;
END;
