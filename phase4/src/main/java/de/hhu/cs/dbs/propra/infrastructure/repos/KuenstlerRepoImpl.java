package de.hhu.cs.dbs.propra.infrastructure.repos;

import static de.hhu.cs.dbs.propra.persistence.sql.sqlite.SQLHelper.executeQuery;
import static de.hhu.cs.dbs.propra.persistence.sql.sqlite.SQLHelper.executeUpdate;

import de.hhu.cs.dbs.propra.domain.model.Aufnahme;
import de.hhu.cs.dbs.propra.domain.model.Kuenstler;
import de.hhu.cs.dbs.propra.domain.model.PremiumNutzer;
import de.hhu.cs.dbs.propra.domain.repos.AufnahmeRepo;
import de.hhu.cs.dbs.propra.domain.repos.KuenstlerRepo;
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
public class KuenstlerRepoImpl implements KuenstlerRepo {

    private final AufnahmeRepo aufnahmeRepo;

    private final PremiumnutzerRepo premiumnutzerRepo;

    public KuenstlerRepoImpl(AufnahmeRepo aufnahmeRepo, PremiumnutzerRepo premiumnutzerRepo) {
        this.aufnahmeRepo = aufnahmeRepo;
        this.premiumnutzerRepo = premiumnutzerRepo;
    }

    @Override
    public List<Kuenstler> getKuenstlerByEmail(String emailFilter, Connection conn)
            throws SQLException {
        String sql =
                "SELECT Künstler.ROWID as kuenstlerID, Pseudonym, Künstler.Benutzername,"
                        + " Aktivitätsdatum  FROM Künstler, Nutzer WHERE Nutzer.Benutzername ="
                        + " Künstler.Benutzername AND Email LIKE ?;";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, emailFilter);
        ResultSet resultSet = executeQuery(conn, hm, sql);
        ArrayList<Kuenstler> kuenstlers = new ArrayList<>();
        while (resultSet.next()) {
            Optional<Kuenstler> kuenstler = kuenstlerMapper(resultSet, conn);
            kuenstler.ifPresent(kuenstlers::add);
        }
        return kuenstlers;
    }

    @Override
    public Optional<Kuenstler> getKuenstlerByBenutzername(String benutzername, Connection conn)
            throws SQLException {
        String sql = "SELECT ROWID as kuenstlerID, * FROM Künstler WHERE Benutzername = ?;";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, benutzername);
        ResultSet resultSet = executeQuery(conn, hm, sql);
        if (resultSet.next()) {
            return kuenstlerMapper(resultSet, conn);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Kuenstler> insertKuenstler(
            String pseudonym, String benutzername, String aktivSeit, Connection conn)
            throws SQLException {
        String sql =
                "INSERT INTO Künstler (Pseudonym, Benutzername, Aktivitätsdatum) VALUES (?,?,?);";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, pseudonym);
        hm.put(2, benutzername);
        hm.put(3, aktivSeit);
        executeUpdate(conn, hm, sql);
        return getKuenstlerByBenutzername(benutzername, conn);
    }

    @Override
    public void kuenstlerInsertAufnahme(String pseudonym, int neueAufnahme, Connection conn)
            throws SQLException {
        String sql = "INSERT INTO Künstler_publiziert_Aufnahme (Künstler, Aufnahme) VALUES (?,?);";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, pseudonym);
        hm.put(2, neueAufnahme);
        executeUpdate(conn, hm, sql);
    }

    @Override
    public ArrayList<Kuenstler> getAllKuenstler(Connection conn) throws SQLException {
        String sql =
                "SELECT Künstler.ROWID as kuenstlerID, Pseudonym, Künstler.Benutzername,"
                        + " Aktivitätsdatum  FROM Künstler, Nutzer WHERE Nutzer.Benutzername ="
                        + " Künstler.Benutzername;";
        HashMap<Integer, Object> hm = new HashMap<>();
        ResultSet resultSet = executeQuery(conn, hm, sql);
        ArrayList<Kuenstler> kuenstlers = new ArrayList<>();
        while (resultSet.next()) {
            Optional<Kuenstler> kuenstler = kuenstlerMapper(resultSet, conn);
            kuenstler.ifPresent(kuenstlers::add);
        }
        return kuenstlers;
    }

    private Optional<Kuenstler> kuenstlerMapper(ResultSet resultSet, Connection conn)
            throws SQLException {
        Optional<PremiumNutzer> premiumNutzer =
                premiumnutzerRepo.getPremiumnutzerByBenutzername(
                        resultSet.getString("Benutzername"), conn);
        List<Aufnahme> aufnahmen =
                aufnahmeRepo.getAufnahmenByKuenstler(resultSet.getString("Pseudonym"), conn);
        return Optional.of(
                new Kuenstler(
                        resultSet.getInt("kuenstlerID"),
                        resultSet.getString("Pseudonym"),
                        resultSet.getString("Aktivitätsdatum"),
                        premiumNutzer.orElse(null),
                        aufnahmen));
    }
}
