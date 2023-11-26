package de.hhu.cs.dbs.propra.domain.repos;

import de.hhu.cs.dbs.propra.domain.model.PremiumNutzer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface PremiumnutzerRepo {

    List<PremiumNutzer> getPremiumnutzerByGueltigkeitstage(
            String gueltigkeitstageFilter, Connection conn) throws SQLException;

    Optional<PremiumNutzer> getPremiumnutzerByBenutzername(String benutzername, Connection conn)
            throws SQLException;

    Optional<PremiumNutzer> insertPremiumnutzer(
            String benutzername, String gueltigkeitstage, Connection conn) throws SQLException;

    ArrayList<PremiumNutzer> getAllPremiumnutzer(Connection conn) throws SQLException;

    void PremiumNutzerErstelltPlaylist(String benutzername, int playlistid, Connection conn)
            throws SQLException;
}
