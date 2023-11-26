package de.hhu.cs.dbs.propra.domain.repos;

import de.hhu.cs.dbs.propra.domain.model.Kuenstler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface KuenstlerRepo {
    List<Kuenstler> getKuenstlerByEmail(String emailFilter, Connection conn) throws SQLException;

    Optional<Kuenstler> getKuenstlerByBenutzername(String benutzername, Connection conn)
            throws SQLException;

    Optional<Kuenstler> insertKuenstler(
            String pseudonym, String benutzername, String aktivSeit, Connection conn)
            throws SQLException;

    void kuenstlerInsertAufnahme(String pseudonym, int neueAufnahme, Connection conn)
            throws SQLException;

    ArrayList<Kuenstler> getAllKuenstler(Connection conn) throws SQLException;
}
