package de.hhu.cs.dbs.propra.infrastructure.repos;

import static de.hhu.cs.dbs.propra.persistence.sql.sqlite.SQLHelper.executeQuery;
import static de.hhu.cs.dbs.propra.persistence.sql.sqlite.SQLHelper.executeUpdate;

import de.hhu.cs.dbs.propra.domain.model.Nutzer;
import de.hhu.cs.dbs.propra.domain.model.Playlist;
import de.hhu.cs.dbs.propra.domain.model.PremiumNutzer;
import de.hhu.cs.dbs.propra.domain.repos.NutzerRepo;
import de.hhu.cs.dbs.propra.domain.repos.PlaylistRepo;
import de.hhu.cs.dbs.propra.domain.repos.PremiumnutzerRepo;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class PremiumnutzerRepoImpl implements PremiumnutzerRepo {

    private final NutzerRepo nutzerRepo;

    private final PlaylistRepo playlistRepo;

    public PremiumnutzerRepoImpl(NutzerRepo nutzerRepo, PlaylistRepo playlistRepo) {
        this.nutzerRepo = nutzerRepo;
        this.playlistRepo = playlistRepo;
    }

    @Override
    public List<PremiumNutzer> getPremiumnutzerByGueltigkeitstage(
            String gueltigkeitstage, Connection conn) throws SQLException {
        String sql = "SELECT ROWID as nutzerid, * FROM PremiumNutzer WHERE Gültigkeitsdauer >= ?;";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, gueltigkeitstage);
        ResultSet resultSet = executeQuery(conn, hm, sql);
        ArrayList<PremiumNutzer> premiumnutzer = new ArrayList<>();
        while (resultSet.next()) {
            Optional<PremiumNutzer> nutzer = premiumnutzerMapper(resultSet, conn);
            nutzer.ifPresent(e -> premiumnutzer.add(e));
        }
        return premiumnutzer;
    }

    @Override
    public Optional<PremiumNutzer> insertPremiumnutzer(
            String benutzername, String gueltigkeitstage, Connection conn) throws SQLException {
        String sql = "INSERT INTO PremiumNutzer (Benutzername, Gültigkeitsdauer) VALUES (?,?);";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, benutzername);
        hm.put(2, gueltigkeitstage);
        executeUpdate(conn, hm, sql);
        return getPremiumnutzerByBenutzername(benutzername, conn);
    }

    @Override
    public Optional<PremiumNutzer> getPremiumnutzerByBenutzername(
            String benutzername, Connection conn) throws SQLException {
        String sql = "SELECT ROWID as nutzerid, * FROM PremiumNutzer WHERE Benutzername = ?;";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, benutzername);
        ResultSet resultSet = executeQuery(conn, hm, sql);
        if (resultSet.next()) {
            return premiumnutzerMapper(resultSet, conn);
        }
        return Optional.empty();
    }

    @Override
    public ArrayList<PremiumNutzer> getAllPremiumnutzer(Connection conn) throws SQLException {
        String sql = "SELECT ROWID as nutzerid, * FROM PremiumNutzer;";
        HashMap<Integer, Object> hm = new HashMap<>();
        ResultSet resultSet = executeQuery(conn, hm, sql);
        ArrayList<PremiumNutzer> premiumnutzer = new ArrayList<>();
        while (resultSet.next()) {
            Optional<PremiumNutzer> nutzer = premiumnutzerMapper(resultSet, conn);
            nutzer.ifPresent(premiumnutzer::add);
        }
        return premiumnutzer;
    }

    @Override
    public void PremiumNutzerErstelltPlaylist(String benutzername, int playlistid, Connection conn)
            throws SQLException {
        String sql =
                "INSERT INTO PremiumNutzer_erstellt_Playlist (PremiumNutzer, Playlist) VALUES"
                        + " (?,?);";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, benutzername);
        hm.put(2, playlistid);
        executeUpdate(conn, hm, sql);
    }

    private Optional<PremiumNutzer> premiumnutzerMapper(ResultSet resultSet, Connection conn)
            throws SQLException {
        Optional<Nutzer> nutzer =
                nutzerRepo.getNutzerByBenutzername(resultSet.getString("Benutzername"), conn);
        List<Playlist> playlists =
                playlistRepo.getPlaylistsByPremiumNutzer(resultSet.getString("Benutzername"), conn);
        return Optional.of(
                new PremiumNutzer(
                        resultSet.getInt("nutzerid"),
                        nutzer.orElse(
                                new Nutzer(
                                        -1,
                                        "dummy",
                                        "dummy@dummy.com",
                                        "password123",
                                        "DE123132123")),
                        resultSet.getString("Gültigkeitsdauer"),
                        playlists));
    }
}
