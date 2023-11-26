package de.hhu.cs.dbs.propra.infrastructure.services;

import static java.util.Optional.ofNullable;

import de.hhu.cs.dbs.propra.domain.errors.APIException;
import de.hhu.cs.dbs.propra.domain.model.*;
import de.hhu.cs.dbs.propra.domain.repos.AufnahmeRepo;
import de.hhu.cs.dbs.propra.domain.repos.PlaylistRepo;
import de.hhu.cs.dbs.propra.domain.repos.PremiumnutzerRepo;
import de.hhu.cs.dbs.propra.domain.repos.SongRepo;
import de.hhu.cs.dbs.propra.domain.services.PremiumNutzerService;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Function;

@Service
public class PremiumNutzerServiceImpl implements PremiumNutzerService {

    private final SQLiteDataSource dataSource;

    private final PlaylistRepo playlistRepo;

    private final PremiumnutzerRepo premiumnutzerRepo;

    private final AufnahmeRepo aufnahmeRepo;

    private final SongRepo songRepo;

    public PremiumNutzerServiceImpl(
            SQLiteDataSource dataSource,
            PlaylistRepo playlistRepo,
            PremiumnutzerRepo premiumnutzerRepo,
            AufnahmeRepo aufnahmeRepo,
            SongRepo songRepo) {
        this.dataSource = dataSource;
        this.playlistRepo = playlistRepo;
        this.premiumnutzerRepo = premiumnutzerRepo;
        this.aufnahmeRepo = aufnahmeRepo;
        this.songRepo = songRepo;
    }

    private static Function<String, String> getFilterString() {
        return s -> "%" + s + "%";
    }

    @Override
    public Optional<Playlist> insertPlaylist(
            String name, Boolean privat, PremiumNutzer premiumNutzer) {
        String nameFilter = ofNullable(name).map(getFilterString()).orElse("%");
        if (name.isEmpty() || privat == null || ofNullable(premiumNutzer).isEmpty()) {
            throw new APIException("All fields must be provided!", HttpStatus.BAD_REQUEST);
        } else {
            try {
                Connection conn = dataSource.getConnection();
                conn.setAutoCommit(false);
                try {
                    Optional<Playlist> playlist = playlistRepo.getPlaylistByExaktName(name, conn);
                    if (playlist.isPresent()) {
                        return playlist;
                    } else {
                        if (playlist.isEmpty()) {
                            playlistRepo.insertPlaylist(name, privat, conn);
                        }
                        Optional<Playlist> neuePlaylist =
                                playlistRepo.getPlaylistByExaktName(name, conn);
                        String benutzername = premiumNutzer.getNutzer().getBenutzername();
                        premiumnutzerRepo.PremiumNutzerErstelltPlaylist(
                                benutzername, neuePlaylist.get().getPlaylistid(), conn);
                        conn.commit();
                        conn.close();
                        return neuePlaylist;
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
    public Optional<Playlist> deletePlaylist(int playlistid, PremiumNutzer premiumNutzer) {
        if (playlistid == 0 || ofNullable(premiumNutzer).isEmpty()) {
            throw new APIException("All fields must be provided!", HttpStatus.BAD_REQUEST);
        } else {
            try {
                Connection conn = dataSource.getConnection();
                conn.setAutoCommit(false);
                try {
                    Optional<Playlist> playlist = playlistRepo.getPlaylistsByID(playlistid, conn);
                    // Überprüfe, ob die Playlist dem PremiumNutzer gehört
                    Optional<Playlist> first =
                            premiumNutzer.getPlaylists().stream()
                                    .filter(p -> p.getPlaylistid() == playlistid)
                                    .findFirst();

                    if (playlist.isEmpty()) {
                        conn.close();
                        throw new APIException(
                                "Playlist existiert nicht mit der ID: " + playlistid,
                                HttpStatus.BAD_REQUEST);
                    } else if (first.isEmpty()) {
                        conn.close();
                        throw new APIException(
                                "Playlist gehört nicht dem PremiumNutzer: "
                                        + premiumNutzer.getNutzer().getBenutzername(),
                                HttpStatus.UNAUTHORIZED);
                    } else {
                        playlistRepo.deletePremiumNutzerErstelltPlaylist(playlistid, conn);
                        playlistRepo.deletePlaylist(playlistid, conn);

                        conn.commit();
                        conn.close();
                        return playlist;
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
    public Optional<Aufnahme> insertAufnahmeInPlaylist(
            int playlistid, int aufnahmeid, PremiumNutzer premiumNutzer) {
        if (playlistid == 0 || aufnahmeid == 0 || ofNullable(premiumNutzer).isEmpty()) {
            throw new APIException("All fields must be provided!", HttpStatus.BAD_REQUEST);
        } else {
            try {
                Connection conn = dataSource.getConnection();
                conn.setAutoCommit(false);
                try {
                    Optional<Playlist> playlist = playlistRepo.getPlaylistsByID(playlistid, conn);
                    Optional<Aufnahme> aufnahme = aufnahmeRepo.getAufnahmeByID(aufnahmeid, conn);
                    // Überprüfe, ob die Playlist dem PremiumNutzer gehört
                    Optional<Playlist> first =
                            premiumNutzer.getPlaylists().stream()
                                    .filter(p -> p.getPlaylistid() == playlistid)
                                    .findFirst();

                    Optional<Aufnahme> second =
                            playlist.get().getAufnahmen().stream()
                                    .filter(aufnahme1 -> aufnahme1.getAufnahmeid() == aufnahmeid)
                                    .findFirst();

                    if (playlist.isEmpty()) {
                        conn.close();
                        throw new APIException(
                                "Playlist existiert nicht mit der ID: " + playlistid,
                                HttpStatus.BAD_REQUEST);
                    } else if (first.isEmpty()) {
                        conn.close();
                        throw new APIException(
                                "Playlist gehört nicht dem PremiumNutzer: "
                                        + premiumNutzer.getNutzer().getBenutzername(),
                                HttpStatus.UNAUTHORIZED);
                    } else if (aufnahme.isEmpty()) {
                        conn.close();
                        throw new APIException(
                                "Aufnahme existiert nicht mit der ID: " + aufnahmeid,
                                HttpStatus.BAD_REQUEST);
                    } else if (second.isPresent()) {
                        conn.close();
                        throw new APIException(
                                "Aufnahme mit der ID "
                                        + aufnahmeid
                                        + " existiert bereits in der Playlist mit der ID "
                                        + playlistid,
                                HttpStatus.BAD_REQUEST);
                    } else {
                        playlistRepo.PlaylistInsertAufnahme(playlistid, aufnahmeid, conn);
                        conn.commit();
                        conn.close();
                        return aufnahme;
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
    public Optional<Aufnahme> deleteAufnahmeFromPlaylist(
            int playlistid, int aufnahmeid, PremiumNutzer premiumNutzer) {
        if (playlistid == 0 || aufnahmeid == 0 || ofNullable(premiumNutzer).isEmpty()) {
            throw new APIException("All fields must be provided!", HttpStatus.BAD_REQUEST);
        } else {
            try {
                Connection conn = dataSource.getConnection();
                conn.setAutoCommit(false);
                try {
                    Optional<Playlist> playlist = playlistRepo.getPlaylistsByID(playlistid, conn);
                    Optional<Aufnahme> aufnahme = aufnahmeRepo.getAufnahmeByID(aufnahmeid, conn);
                    // Überprüfe, ob die Playlist dem PremiumNutzer gehört
                    Optional<Playlist> first =
                            premiumNutzer.getPlaylists().stream()
                                    .filter(p -> p.getPlaylistid() == playlistid)
                                    .findFirst();

                    if (playlist.isEmpty()) {
                        conn.close();
                        throw new APIException(
                                "Playlist existiert nicht mit der ID: " + playlistid,
                                HttpStatus.BAD_REQUEST);
                    } else if (first.isEmpty()) {
                        conn.close();
                        throw new APIException(
                                "Playlist gehört nicht dem PremiumNutzer: "
                                        + premiumNutzer.getNutzer().getBenutzername(),
                                HttpStatus.UNAUTHORIZED);
                    } else if (aufnahme.isEmpty()) {
                        conn.close();
                        throw new APIException(
                                "Aufnahme existiert nicht mit der ID: " + aufnahmeid,
                                HttpStatus.BAD_REQUEST);
                    } else {
                        playlistRepo.deleteAufnahmeFromPlaylist(playlistid, aufnahmeid, conn);
                        conn.commit();
                        conn.close();
                        return aufnahme;
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
