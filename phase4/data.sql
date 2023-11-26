INSERT INTO Nutzer (Benutzername, Email, IBAN, Passwort) VALUES 
    ('dehus10', 'DennisHuss@hhu.de', 'DE02120300000000202051', 'Pas2word1'),
    ('Known11', 'alfredHitchcock@gmail.com', 'DE02500105170137075030', 'Pas2word2'),
    ('Woozy12', 'johnFavreau@disney.com', 'DE02100500000054540402', 'th8bIrds2'),
    ('Meana13', 'christopherNolan@gmail.com', 'DE02300209000106531065', 'pul55iCti'),
    ('Yelle14', 'quentinTarantino@hotmail.com', 'DE02200505501015871393', 't1eDa4kni'),
    ('Dengo55', 'jamesCameron@tonline.de', 'DE03300505402015871365', 'FgkQ5jz6k'),
    ('Hermi44', 'hermineGrainger@gmx.de', 'DE03334848838823829323', 'theFjmn33');


INSERT INTO PremiumNutzer (Benutzername, Gültigkeitsdauer) VALUES 
    ('dehus10', '2024-12-05 00:00:00'),
    ('Known11', '2024-10-15 00:00:00'),
    ('Yelle14', '2025-03-03 00:00:00'),
    ('Dengo55', '2025-04-04 00:00:00'),
    ('Hermi44', '2025-05-01 00:00:00');

INSERT INTO Künstler (Pseudonym, Benutzername, Aktivitätsdatum) VALUES
    ('Luffy', 'dehus10', '2024-09-05 00:00:00'),
    ('Zorro', 'Known11', '2024-08-04 00:00:00'),
    ('Sanji', 'Yelle14', '2025-07-07 00:00:00'),
    ('Lyssop', 'Dengo55', '2025-04-03 00:00:00'),
    ('Eminem', 'Hermi44', '2030-05-30 00:00:00');

INSERT INTO Podcaster (Pseudonym, Level) VALUES
    ('Luffy', TRUE),  --professionell
    ('Zorro', FALSE);  -- nicht professionell

INSERT INTO Musiker (Pseudonym, auftrittfähig) VALUES
    ('Luffy', TRUE),
    ('Zorro', FALSE),
    ('Sanji', TRUE),
    ('Lyssop', TRUE),
    ('Eminem', FALSE);


INSERT INTO Band (Name, Bandgeschichte) VALUES
    ('The Beatles', 'Die The Beatles Geschichte');


INSERT INTO Playlist (Name, Sichtbarkeit) VALUES
    ('Deutschrap Brandneu', TRUE),
    ('Party Playlist', TRUE),
    ('80er Playlist', FALSE),
    ('2000er', TRUE);

INSERT INTO Album (Bezeichnung, Erscheinungsjahr) VALUES
    ('JBG', 2019),
    ('MIB', 2018),
    ('Battleground', 2019),
    ('Sweetbitter', 2019),
    ('rubber soul', 1965),
    ('The Eminem Show', 2002);


INSERT INTO Kritik (Text, Benutzername, Album) VALUES
    ('Musik langweilig. Kein Hit', 'dehus10', 1 ),
    ('Liedtexte oberflaechlich. Keine Tiefe.', 'Woozy12', 2);

INSERT INTO Aufnahme (Dauer, Titel) VALUES
    ( 120, 'Allein'),
    ( 625, 'Rolex'),
    ( 90, 'Abfahrt'),
    ( 45, 'Oracle'),
    ( 187, 'Druff'),
    ( 111, 'lolipop'),
    ( 463, 'Gehirnmatsche'),
    ( 182, 'Wir sind zurück'),
    ( 98, 'Drive my Car'),
    ( 69, 'Norwegian Wood');


INSERT INTO Podcast (Name) VALUES
    ('Serienkiller'),
    ('Dick & Doof'),
    ('Erfolg im Studium'),
    ('Erfolgreich Studieren');

INSERT INTO Sprache (Bezeichnung) VALUES
     ('Deutsch'),
     ('Italienisch'),
     ('Franzakisch'),
     ('Spagnol');


INSERT INTO PodcastEpisode (Nummer, Podcast, Sprache) VALUES
    (1, 1, 1),
    (2, 1, 2),
    (3, 1, 3);

INSERT INTO Thema (Name) VALUES
   ('Thriller'),
   ('Comedy'),
   ('Studium');

INSERT INTO Genre (Bezeichnung) VALUES
    ('Pop'),
    ('Rock'),
    ('Deutschrap');

INSERT INTO Song (ID, SpeicherortLQ, SpeicherortHQ, Genre) VALUES
   (9, '/one/yellow-submarineLQ.mp3', '/one/yellow-submarineHQ.mp3', 1),
   (10, '/one/yesterdayLQ.mp3','/one/yesterdayHQ.mp3',2),
   (3, '/two/yesterdayLQ.mp3','/two/yesterdayHQ.mp3',3),
   (5, '/three/yesterdayLQ.mp3','/three/yesterdayHQ.mp3',3),
   (1, '/four/yesterdayLQ.mp3','/four/yesterdayHQ.mp3',3);

INSERT INTO Nutzer_like_dislike_Playlist (Nutzer, Playlist, Wert) VALUES
    ('Meana13', 1, 'like'),
    ('Dengo55', 2, 'dislike'),
    ('Known11', 2, 'dislike'),
    ('Woozy12', 2, 'dislike'),
    ('Dengo55', 4, 'like'),
    ('Dengo55', 3, 'like');

INSERT INTO Song_Bestandteil_von_Album (Song, Album) VALUES
     (10, 5);

INSERT INTO Nutzer_bewertet_Song (Nutzer, Song, Bewertung) VALUES
   ('Meana13', 9, '5'),
   ('Woozy12', 9, '1'),
   ('Dengo55', 9, '5');

INSERT INTO PremiumNutzer_erstellt_Playlist (PremiumNutzer, Playlist) VALUES
    ('dehus10', 1),
    ('Known11', 2),
    ('Yelle14', 3),
    ('Yelle14', 4);

INSERT INTO Playlist_enthält_Aufnahme (Playlist, Aufnahme) VALUES
    (1,1),
    (2,3),
    (2,5),
    (4,6);

INSERT INTO Podcast_behandelt_Thema (Podcast, Thema) VALUES
    (1,3),
    (2,2),
    (3,3),
    (4,3);

INSERT INTO Künstler_publiziert_Aufnahme (Künstler, Aufnahme) VALUES
    ('Luffy', 7),
    ('Zorro', 8),
    ('Sanji', 1),
    ('Lyssop', 2),
    ('Sanji', 3),
    ('Lyssop', 4),
    ('Sanji', 5);

INSERT INTO Podcaster_arbeitet_mit (Podcaster1, Podcaster2) VALUES
    ('Luffy', 'Zorro');

INSERT INTO Band_besteht_aus_Musiker (Band, Musiker) VALUES
    ('The Beatles', 'Eminem'),
    ('The Beatles', 'Zorro'),
    ('The Beatles', 'Sanji');


INSERT INTO Musiker_veröffentlicht_Album (Musiker, Album) VALUES
    ('Sanji',1),
    ('Lyssop',2),
    ('Eminem',6);



INSERT INTO Band_veröffentlicht_Album (Band, Album) VALUES
    ('The Beatles', 5);







