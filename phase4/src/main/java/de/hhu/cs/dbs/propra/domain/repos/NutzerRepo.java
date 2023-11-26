package de.hhu.cs.dbs.propra.domain.repos;

import de.hhu.cs.dbs.propra.domain.model.Nutzer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public interface NutzerRepo {

    ArrayList<Nutzer> getNutzerByEmailAndIBAN(String email, String iban, Connection connection)
            throws SQLException;

    ArrayList<Nutzer> getAllNutzer(Connection connection) throws SQLException;

    Optional<Nutzer> getNutzerByEmail(String email, Connection conn) throws SQLException;

    Optional<Nutzer> insertNutzer(
            String email, String passwort, String benutzername, String iban, Connection conn)
            throws SQLException;

    Optional<Nutzer> getNutzerByBenutzername(String benutzername, Connection conn)
            throws SQLException;
}
