SELECT Nutzer.Benutzername, Nutzer.Email
FROM Nutzer
LEFT JOIN PremiumNutzer ON Nutzer.Benutzername = PremiumNutzer.Benutzername
WHERE PremiumNutzer.Benutzername IS NULL;

/*
Benutzername: 'Woozy12', E-Mail: 'johnFavreau@disney.com'
Benutzername: 'Meana13', E-Mail: 'christopherNolan@gmail.com'
 */

SELECT Song.ID, Song.Genre, Song.SpeicherortHQ, Song.SpeicherortLQ
FROM Song, Genre, Aufnahme
WHERE Aufnahme.ID = Song.ID
AND Song.Genre = Genre.ID
AND Genre.Bezeichnung = 'Rock'
ORDER BY Aufnahme.Dauer;

/*
ID: 10, Genre: 2, SpeicherortHQ: '/one/yesterdayHQ.mp3', SpeicherortLQ: '/one/yesterdayLQ.mp3'
 */

SELECT p.ID, p.Name
FROM PodcastEpisode pe
         JOIN Podcast p ON pe.Podcast = p.ID
         JOIN Podcast_behandelt_Thema pt ON p.ID = pt.Podcast
         JOIN Thema t ON pt.Thema = t.ID
WHERE t.Name = 'Studium'
GROUP BY p.ID, p.Name
HAVING COUNT(pe.ID) >= 3;

/*
 ID: 1, Name: Serienkiller
 */

