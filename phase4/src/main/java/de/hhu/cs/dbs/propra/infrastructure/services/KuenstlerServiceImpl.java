package de.hhu.cs.dbs.propra.infrastructure.services;

import static java.util.Optional.ofNullable;

import de.hhu.cs.dbs.propra.domain.errors.APIException;
import de.hhu.cs.dbs.propra.domain.model.Aufnahme;
import de.hhu.cs.dbs.propra.domain.model.Kuenstler;
import de.hhu.cs.dbs.propra.domain.model.Song;
import de.hhu.cs.dbs.propra.domain.repos.AufnahmeRepo;
import de.hhu.cs.dbs.propra.domain.repos.KuenstlerRepo;
import de.hhu.cs.dbs.propra.domain.repos.SongRepo;
import de.hhu.cs.dbs.propra.domain.services.KuenstlerService;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Function;

@Service
public class KuenstlerServiceImpl implements KuenstlerService {

    private final KuenstlerRepo kuenstlerRepo;

    private final AufnahmeRepo aufnahmeRepo;

    private final SongRepo songRepo;

    private final SQLiteDataSource dataSource;

    public KuenstlerServiceImpl(
            KuenstlerRepo kuenstlerRepo,
            AufnahmeRepo aufnahmeRepo,
            SongRepo songRepo,
            SQLiteDataSource dataSource) {
        this.kuenstlerRepo = kuenstlerRepo;
        this.aufnahmeRepo = aufnahmeRepo;
        this.songRepo = songRepo;
        this.dataSource = dataSource;
    }

    private static Function<String, String> getFilterString() {
        return s -> "%" + s + "%";
    }

    @Override
    public Optional<Song> insertSong(
            String titel,
            int dauer,
            String speicherortHq,
            String speicherortLq,
            Integer genreid,
            Kuenstler kuenstler) {
        String titelFilter = ofNullable(titel).map(getFilterString()).orElse("%");
        if (titel.isEmpty()
                || dauer == 0
                || speicherortHq.isEmpty()
                || speicherortLq.isEmpty()
                || ofNullable(kuenstler).isEmpty()) {
            throw new APIException("All fields must be provided!", HttpStatus.BAD_REQUEST);
        } else {
            try {
                Connection conn = dataSource.getConnection();
                conn.setAutoCommit(false);
                try {
                    int genre =
                            ofNullable(genreid).orElse(songRepo.getDummyGenre(conn).getGenreid());
                    Optional<Song> song = songRepo.getSongByTitel(titel, conn);
                    Optional<Aufnahme> aufnahmeByExaktTitel =
                            aufnahmeRepo.getAufnahmeByExaktTitel(titel, conn);
                    if (song.isPresent() && aufnahmeByExaktTitel.isPresent()) {
                        return song;
                    } else if (song.isPresent()) {
                        conn.close();
                        throw new APIException(
                                "Song existiert bereits mit dem Titel: " + titelFilter,
                                HttpStatus.BAD_REQUEST);
                    } else {
                        if (aufnahmeByExaktTitel.isEmpty()) {
                            aufnahmeRepo.insertAufnahme(titel, dauer, conn);
                        }
                        Aufnahme aufnahme =
                                aufnahmeRepo.getAufnahmeByExaktTitel(titel, conn).orElseThrow();
                        kuenstlerRepo.kuenstlerInsertAufnahme(
                                kuenstler.getPseudonym(), aufnahme.getAufnahmeid(), conn);
                        songRepo.insertSong(speicherortHq, speicherortLq, genre, conn);
                        Optional<Song> neuerSong = songRepo.getSongByTitel(titelFilter, conn);
                        conn.commit();
                        conn.close();
                        return neuerSong;
                    }
                } catch (SQLException e) {
                    conn.rollback();
                    throw new APIException(e.getMessage(), HttpStatus.BAD_REQUEST);
                }
            } catch (SQLException e) {
                throw new APIException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @Override
    public Optional<Song> deleteSong(Integer songid, Kuenstler kuenstler) {
        if (songid == 0 || ofNullable(kuenstler).isEmpty()) {
            throw new APIException("All fields must be provided!", HttpStatus.BAD_REQUEST);
        } else {
            try {
                Connection conn = dataSource.getConnection();
                conn.setAutoCommit(false);
                try {
                    Optional<Song> song = songRepo.getSongById(songid, conn);
                    // Überprüfe, ob der Song dem Künstler gehört
                    Optional<Aufnahme> first =
                            kuenstler.getAufnahmen().stream()
                                    .filter(aufnahme -> aufnahme.getAufnahmeid() == songid)
                                    .findFirst();
                    if (song.isEmpty()) {
                        conn.close();
                        throw new APIException(
                                "Song existiert nicht mit der ID: " + songid,
                                HttpStatus.BAD_REQUEST);
                    } else if (first.isEmpty()) {
                        conn.close();
                        throw new APIException(
                                "Song gehört nicht dem Künstler: " + kuenstler.getPseudonym(),
                                HttpStatus.UNAUTHORIZED);
                    } else {
                        Optional<Aufnahme> deleteAufnahme =
                                kuenstler.getAufnahmen().stream()
                                        .filter(aufnahme -> aufnahme.getAufnahmeid() == songid)
                                        .findFirst();
                        int deleteAufnahmeID = deleteAufnahme.get().getAufnahmeid();
                        songRepo.deleteSong(songid, conn);
                        aufnahmeRepo.deleteAufnahme(deleteAufnahmeID, conn);
                        aufnahmeRepo.deleteKuenstler_publiziert_Aufnahme(deleteAufnahmeID, conn);
                        conn.commit();
                        conn.close();
                        return song;
                    }
                } catch (SQLException e) {
                    conn.rollback();
                    throw new APIException(e.getMessage(), HttpStatus.BAD_REQUEST);
                }
            } catch (SQLException e) {
                throw new APIException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
}
