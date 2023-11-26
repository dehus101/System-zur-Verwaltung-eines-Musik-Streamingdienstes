package de.hhu.cs.dbs.propra.infrastructure.services;

import de.hhu.cs.dbs.propra.domain.errors.APIException;
import de.hhu.cs.dbs.propra.domain.model.*;
import de.hhu.cs.dbs.propra.domain.repos.*;
import de.hhu.cs.dbs.propra.domain.services.AnwenderService;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class AnwenderServiceImpl implements AnwenderService {

    private final NutzerRepo nutzerRepo;
    private final PremiumnutzerRepo premiumnutzerRepo;
    private final KuenstlerRepo kuenstlerRepo;

    private final AufnahmeRepo aufnahmeRepo;
    private final PlaylistRepo playlistRepo;

    private final BandsRepo bandRepo;

    private final SongRepo songsRepo;

    private final AlbenRepo albenRepo;

    private final SQLiteDataSource dataSource;

    public AnwenderServiceImpl(
            NutzerRepo nutzerRepo,
            PremiumnutzerRepo premiumnutzerRepo,
            KuenstlerRepo kuenstlerRepo,
            AufnahmeRepo aufnahmeRepo,
            PlaylistRepo playlistRepo,
            BandsRepo bandRepo,
            SongRepo songsRepo,
            AlbenRepo albenRepo,
            SQLiteDataSource dataSource) {
        this.nutzerRepo = nutzerRepo;
        this.premiumnutzerRepo = premiumnutzerRepo;
        this.kuenstlerRepo = kuenstlerRepo;
        this.aufnahmeRepo = aufnahmeRepo;
        this.playlistRepo = playlistRepo;
        this.bandRepo = bandRepo;
        this.songsRepo = songsRepo;
        this.albenRepo = albenRepo;
        this.dataSource = dataSource;
    }

    private static Function<String, String> getFilterString() {
        return s -> "%" + s + "%";
    }

    private static Function<Integer, String> berechneNeuesDatum() {
        return gueltigkeitstage -> {
            LocalDate aktuellesDatum = LocalDate.now();
            LocalDate neuesDatum = aktuellesDatum.plusDays(gueltigkeitstage);

            LocalDateTime neuesDatumMitUhrzeit = neuesDatum.atTime(LocalTime.MIDNIGHT);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return neuesDatumMitUhrzeit.format(formatter);
        };
    }

    @Override
    public List<Nutzer> getAllNutzerWithFilter(String email, String iban) throws APIException {
        String emailFilter = Optional.ofNullable(email).map(getFilterString()).orElse("%");
        String ibanFilter = Optional.ofNullable(iban).map(getFilterString()).orElse("%");
        try (Connection conn = dataSource.getConnection()) {
            try {
                return nutzerRepo.getNutzerByEmailAndIBAN(emailFilter, ibanFilter, conn);
            } catch (SQLException e) {
                throw new APIException(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        } catch (SQLException e) {
            throw new APIException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Optional<Nutzer> insertNutzer(
            String email, String passwort, String benutzername, String iban) throws APIException {
        // If one of the fields is empty, throw an exception
        if (email.isEmpty() || passwort.isEmpty() || benutzername.isEmpty() || iban.isEmpty()) {
            throw new APIException("All fields must be provided!", HttpStatus.BAD_REQUEST);
        } else {
            try {
                Connection conn = dataSource.getConnection();
                conn.setAutoCommit(false);
                try {
                    Optional<Nutzer> nutzer = nutzerRepo.getNutzerByEmail(email, conn);
                    if (nutzer.isPresent()) {
                        return nutzer;
                    } else {
                        nutzerRepo.insertNutzer(email, passwort, benutzername, iban, conn);
                        conn.commit();
                        return nutzerRepo.getNutzerByEmail(email, conn);
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
    public List<PremiumNutzer> getAllPremiumnutzerWithFilter(Integer gueltigkeitstage) {
        // Nur Premiumnutzer, die noch mindestens gueltigkeitstage Tage Premiumnutzer sind, werden
        // zurückgegeben.

        String gueltigkeitstageFilter =
                Optional.ofNullable(gueltigkeitstage).map(berechneNeuesDatum()).orElse("%");
        try (Connection conn = dataSource.getConnection()) {
            try {
                return premiumnutzerRepo.getPremiumnutzerByGueltigkeitstage(
                        gueltigkeitstageFilter, conn);
            } catch (SQLException e) {
                throw new APIException(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        } catch (SQLException e) {
            throw new APIException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Optional<PremiumNutzer> insertPremiumnutzer(
            String email,
            String passwort,
            String benutzername,
            String iban,
            String gueltigkeitstage) {
        // If one of the fields is empty, throw an exception
        if (email.isEmpty()
                || passwort.isEmpty()
                || benutzername.isEmpty()
                || iban.isEmpty()
                || gueltigkeitstage.isEmpty()) {
            throw new APIException("All fields must be provided!", HttpStatus.BAD_REQUEST);
        } else {
            try {
                Connection conn = dataSource.getConnection();
                conn.setAutoCommit(false);
                try {
                    Optional<PremiumNutzer> premiumnutzer =
                            premiumnutzerRepo.getPremiumnutzerByBenutzername(benutzername, conn);
                    Optional<Nutzer> nutzer =
                            nutzerRepo.getNutzerByBenutzername(benutzername, conn);
                    if (premiumnutzer.isPresent() && nutzer.isPresent()) {
                        return premiumnutzer;
                    } else if (premiumnutzer.isPresent()) {
                        conn.close();
                        throw new APIException(
                                "Premiumnutzer existiert bereits mit dem Benutzernamen: "
                                        + benutzername,
                                HttpStatus.BAD_REQUEST);
                    } else {
                        if (nutzer.isEmpty()) {
                            nutzerRepo.insertNutzer(email, passwort, benutzername, iban, conn);
                        }
                        Optional<PremiumNutzer> premiumNutzer =
                                premiumnutzerRepo.insertPremiumnutzer(
                                        benutzername, gueltigkeitstage, conn);
                        conn.commit();
                        conn.close();
                        return premiumNutzer;
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
    public List<Kuenstler> getAllKuenstlerWithFilter(String email) {
        String emailFilter = Optional.ofNullable(email).map(getFilterString()).orElse("%");
        try (Connection conn = dataSource.getConnection()) {
            try {
                return kuenstlerRepo.getKuenstlerByEmail(emailFilter, conn);
            } catch (SQLException e) {
                throw new APIException(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        } catch (SQLException e) {
            throw new APIException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Optional<Kuenstler> insertKuenstler(
            String email,
            String passwort,
            String benutzername,
            String iban,
            String gueltigkeitstage,
            String aktivSeit,
            String pseudonym) {
        if (email.isEmpty()
                || passwort.isEmpty()
                || benutzername.isEmpty()
                || iban.isEmpty()
                || pseudonym.isEmpty()
                || gueltigkeitstage.isEmpty()) {
            throw new APIException("All fields must be provided!", HttpStatus.BAD_REQUEST);
        } else {
            try {
                Connection conn = dataSource.getConnection();
                conn.setAutoCommit(false);
                try {
                    Optional<Kuenstler> kuenstler =
                            kuenstlerRepo.getKuenstlerByBenutzername(benutzername, conn);
                    Optional<PremiumNutzer> premiumnutzer =
                            premiumnutzerRepo.getPremiumnutzerByBenutzername(benutzername, conn);
                    Optional<Nutzer> nutzer =
                            nutzerRepo.getNutzerByBenutzername(benutzername, conn);
                    if (kuenstler.isPresent() && premiumnutzer.isPresent() && nutzer.isPresent()) {
                        return kuenstler;
                    } else if (kuenstler.isPresent()) {
                        conn.close();
                        throw new APIException(
                                "Künstler existiert bereits mit dem Benutzernamen: " + benutzername,
                                HttpStatus.BAD_REQUEST);
                    } else {
                        if (nutzer.isEmpty()) {
                            nutzerRepo.insertNutzer(email, passwort, benutzername, iban, conn);
                        }

                        premiumnutzerRepo.insertPremiumnutzer(benutzername, gueltigkeitstage, conn);
                        Optional<Kuenstler> insertKuenstler =
                                kuenstlerRepo.insertKuenstler(
                                        pseudonym, benutzername, aktivSeit, conn);

                        conn.commit();
                        conn.close();
                        return insertKuenstler;
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
    public List<Song> getSongs() {
        try (Connection conn = dataSource.getConnection()) {
            try {
                return songsRepo.getSongs(conn);
            } catch (SQLException e) {
                throw new APIException(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        } catch (SQLException e) {
            throw new APIException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<Album> getAlben() {
        try (Connection conn = dataSource.getConnection()) {
            try {
                return albenRepo.getAlben(conn);
            } catch (SQLException e) {
                throw new APIException(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        } catch (SQLException e) {
            throw new APIException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<Band> getBands(String name) {
        String nameFilter = Optional.ofNullable(name).map(getFilterString()).orElse("%");
        try (Connection conn = dataSource.getConnection()) {
            try {
                return bandRepo.getBands(nameFilter, conn);
            } catch (SQLException e) {
                throw new APIException(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        } catch (SQLException e) {
            throw new APIException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<Playlist> getPlaylists(String name) {
        String nameFilter = Optional.ofNullable(name).map(getFilterString()).orElse("%");
        try (Connection conn = dataSource.getConnection()) {
            try {
                return playlistRepo.getPlaylists(nameFilter, conn);
            } catch (SQLException e) {
                throw new APIException(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        } catch (SQLException e) {
            throw new APIException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // playlists/{playlistid}/aufnahmen
    @Override
    public List<Aufnahme> getAufnahmenByPlaylistsByID(String titel, int playlistId) {
        String nameFilter = Optional.ofNullable(titel).map(getFilterString()).orElse("%");
        try (Connection conn = dataSource.getConnection()) {
            try {
                return aufnahmeRepo.getAufnahmenByPlaylistsID(playlistId, nameFilter, conn);
            } catch (SQLException e) {
                throw new APIException(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        } catch (SQLException e) {
            throw new APIException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<Kritik> getKritikenByAlbumID(int albumId) {
        try (Connection conn = dataSource.getConnection()) {
            try {
                return albenRepo.getKritikenByAlbumID(albumId, conn);
            } catch (SQLException e) {
                throw new APIException(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        } catch (SQLException e) {
            throw new APIException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Optional<Kuenstler> getKuenstlerByUsername(String username) {
        try (Connection conn = dataSource.getConnection()) {
            try {
                return kuenstlerRepo.getKuenstlerByBenutzername(username, conn);
            } catch (SQLException e) {
                throw new APIException(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        } catch (SQLException e) {
            throw new APIException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Optional<PremiumNutzer> getPremiumNutzerByUsername(String username) {
        try (Connection conn = dataSource.getConnection()) {
            try {
                return premiumnutzerRepo.getPremiumnutzerByBenutzername(username, conn);
            } catch (SQLException e) {
                throw new APIException(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        } catch (SQLException e) {
            throw new APIException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
