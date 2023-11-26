package de.hhu.cs.dbs.propra.domain.repos;

import de.hhu.cs.dbs.propra.domain.model.Aufnahme;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface AufnahmeRepo {
    List<Aufnahme> getAufnahmeByTitle(String title, Connection conn) throws SQLException;

    List<Aufnahme> getAufnahmenByPlaylistsID(int playlistId, String titel, Connection conn)
            throws SQLException;

    Optional<Aufnahme> getAufnahmeByExaktTitel(String title, Connection conn) throws SQLException;

    Optional<Aufnahme> insertAufnahme(String titelFilter, int dauer, Connection conn)
            throws SQLException;

    List<Aufnahme> getAufnahmenByKuenstler(String pseudonym, Connection conn) throws SQLException;

    void deleteAufnahme(int deleteAufnahme, Connection conn) throws SQLException;

    void deleteKuenstler_publiziert_Aufnahme(int deleteAufnahmeID, Connection conn)
            throws SQLException;

    Optional<Aufnahme> getAufnahmeByID(int aufnahmeid, Connection conn) throws SQLException;

    List<Aufnahme> getAufnahmeByPlaylistID(String playlistid, Connection conn) throws SQLException;
}
