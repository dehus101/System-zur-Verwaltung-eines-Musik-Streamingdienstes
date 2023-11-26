package de.hhu.cs.dbs.propra.infrastructure.repos;

import static de.hhu.cs.dbs.propra.persistence.sql.sqlite.SQLHelper.executeQuery;
import static de.hhu.cs.dbs.propra.persistence.sql.sqlite.SQLHelper.executeUpdate;

import de.hhu.cs.dbs.propra.domain.model.Nutzer;
import de.hhu.cs.dbs.propra.domain.repos.NutzerRepo;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

@Repository
public class NutzerRepoImpl implements NutzerRepo {

    public ArrayList<Nutzer> getNutzerByEmailAndIBAN(
            String email, String iban, Connection connection) throws SQLException {
        String sql = "SELECT ROWID as nutzerid, * FROM Nutzer WHERE Email LIKE ? AND IBAN LIKE ?;";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, email);
        hm.put(2, iban);
        ResultSet resultSet = executeQuery(connection, hm, sql);
        ArrayList<Nutzer> nutzers = new ArrayList<>();
        while (resultSet.next()) {
            Optional<Nutzer> nutzer = nutzerMapper(resultSet);
            nutzer.ifPresent(nutzers::add);
        }
        return nutzers;
    }

    @Override
    public ArrayList<Nutzer> getAllNutzer(Connection connection) throws SQLException {
        String sql = "SELECT ROWID as nutzerid, * FROM Nutzer;";
        HashMap<Integer, Object> hm = new HashMap<>();
        ResultSet resultSet = executeQuery(connection, hm, sql);
        ArrayList<Nutzer> nutzers = new ArrayList<>();
        while (resultSet.next()) {
            Optional<Nutzer> nutzer = nutzerMapper(resultSet);
            nutzer.ifPresent(nutzers::add);
        }
        return nutzers;
    }

    @Override
    public Optional<Nutzer> getNutzerByEmail(String email, Connection conn) throws SQLException {
        String sql = "SELECT ROWID as nutzerid, * FROM Nutzer WHERE Email = ?;";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, email);
        ResultSet resultSet = executeQuery(conn, hm, sql);
        return (!resultSet.next()) ? Optional.empty() : nutzerMapper(resultSet);
    }

    @Override
    public Optional<Nutzer> insertNutzer(
            String email, String passwort, String benutzername, String iban, Connection conn)
            throws SQLException {
        String sql = "INSERT INTO Nutzer (Email, Passwort, Benutzername, IBAN) VALUES (?,?,?,?);";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, email);
        hm.put(2, passwort);
        hm.put(3, benutzername);
        hm.put(4, iban);
        executeUpdate(conn, hm, sql);
        return getNutzerByEmail(email, conn);
    }

    @Override
    public Optional<Nutzer> getNutzerByBenutzername(String benutzername, Connection conn)
            throws SQLException {
        String sql = "SELECT ROWID as nutzerid, * FROM Nutzer WHERE Benutzername = ?;";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, benutzername);
        ResultSet resultSet = executeQuery(conn, hm, sql);
        return (!resultSet.next()) ? Optional.empty() : nutzerMapper(resultSet);
    }

    private Optional<Nutzer> getNutzerById(long nutzerid, Connection conn) throws SQLException {
        String sql = "SELECT ROWID as nutzerid, * FROM Nutzer WHERE nutzerid = ?;";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, nutzerid);
        ResultSet resultSet = executeQuery(conn, hm, sql);
        return (!resultSet.next()) ? Optional.empty() : nutzerMapper(resultSet);
    }

    private Optional<Nutzer> nutzerMapper(ResultSet resultSet) throws SQLException {
        return Optional.of(
                new Nutzer(
                        resultSet.getInt("nutzerid"),
                        resultSet.getString("Benutzername"),
                        resultSet.getString("Email"),
                        resultSet.getString("Passwort"),
                        resultSet.getString("Iban")));
    }
}
