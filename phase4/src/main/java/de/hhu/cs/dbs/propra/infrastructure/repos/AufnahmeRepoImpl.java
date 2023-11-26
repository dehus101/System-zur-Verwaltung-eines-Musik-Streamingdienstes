package de.hhu.cs.dbs.propra.infrastructure.repos;

import static de.hhu.cs.dbs.propra.persistence.sql.sqlite.SQLHelper.executeQuery;
import static de.hhu.cs.dbs.propra.persistence.sql.sqlite.SQLHelper.executeUpdate;

import de.hhu.cs.dbs.propra.domain.model.Aufnahme;
import de.hhu.cs.dbs.propra.domain.repos.AufnahmeRepo;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class AufnahmeRepoImpl implements AufnahmeRepo {
    @Override
    public List<Aufnahme> getAufnahmeByTitle(String title, Connection conn) throws SQLException {
        String sql = "SELECT ROWID as aufnahmeid, * FROM Aufnahme WHERE Titel LIKE ?;";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, title);
        ResultSet resultSet = executeQuery(conn, hm, sql);
        ArrayList<Aufnahme> aufnahmen = new ArrayList<>();
        while (resultSet.next()) {
            Optional<Aufnahme> aufnahme = aufnahmeMapper(resultSet);
            aufnahme.ifPresent(aufnahmen::add);
        }
        return aufnahmen;
    }

    public Optional<Aufnahme> getAufnahmeByExaktTitel(String title, Connection conn)
            throws SQLException {
        String sql = "SELECT ROWID as aufnahmeid, * FROM Aufnahme WHERE Titel = ?;";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, title);
        ResultSet resultSet = executeQuery(conn, hm, sql);
        return (!resultSet.next()) ? Optional.empty() : aufnahmeMapper(resultSet);
    }

    @Override
    public Optional<Aufnahme> insertAufnahme(String titelFilter, int dauer, Connection conn)
            throws SQLException {
        String sql = "INSERT INTO Aufnahme (Dauer, Titel) VALUES (?,?);";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, dauer);
        hm.put(2, titelFilter);
        executeUpdate(conn, hm, sql);
        return getAufnahmeByExaktTitel(titelFilter, conn);
    }

    @Override
    public List<Aufnahme> getAufnahmenByPlaylistsID(int playlistId, String titel, Connection conn)
            throws SQLException {
        String sql =
                "SELECT Aufnahme.ROWID as aufnahmeid, Aufnahme.Dauer as Dauer, Aufnahme.Titel as"
                    + " Titel\n"
                    + "FROM Playlist, Aufnahme INNER JOIN Playlist_enthält_Aufnahme ON Aufnahme.ID"
                    + " = Playlist_enthält_Aufnahme.Aufnahme\n"
                    + "WHERE Aufnahme.Titel LIKE ? \n"
                    + "AND Playlist.ID = Playlist_enthält_Aufnahme.Playlist\n"
                    + "AND Playlist.ID = ?";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, titel);
        hm.put(2, playlistId);
        ResultSet resultSet = executeQuery(conn, hm, sql);
        ArrayList<Aufnahme> aufnahmen = new ArrayList<>();
        while (resultSet.next()) {
            Optional<Aufnahme> aufnahme = aufnahmeMapper(resultSet);
            aufnahme.ifPresent(aufnahmen::add);
        }
        return aufnahmen;
    }

    @Override
    public List<Aufnahme> getAufnahmenByKuenstler(String pseudonym, Connection conn)
            throws SQLException {
        String sql =
                "SELECT Aufnahme.ID as aufnahmeID, * FROM Aufnahme, Künstler_publiziert_Aufnahme"
                        + " WHERE Aufnahme.ID = Künstler_publiziert_Aufnahme.Aufnahme AND"
                        + " Künstler_publiziert_Aufnahme.Künstler = ?;";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, pseudonym);
        ResultSet resultSet = executeQuery(conn, hm, sql);
        ArrayList<Aufnahme> aufnahmen = new ArrayList<>();
        while (resultSet.next()) {
            Optional<Aufnahme> aufnahme = aufnahmeMapper(resultSet);
            aufnahme.ifPresent(aufnahmen::add);
        }
        return aufnahmen;
    }

    @Override
    public void deleteAufnahme(int deleteAufnahme, Connection conn) throws SQLException {
        String sql = "DELETE FROM Aufnahme WHERE ID = ?;";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, deleteAufnahme);
        executeUpdate(conn, hm, sql);
    }

    @Override
    public void deleteKuenstler_publiziert_Aufnahme(int deleteAufnahmeID, Connection conn)
            throws SQLException {
        String sql = "DELETE FROM Künstler_publiziert_Aufnahme WHERE Aufnahme = ?;";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, deleteAufnahmeID);
        executeUpdate(conn, hm, sql);
    }

    @Override
    public Optional<Aufnahme> getAufnahmeByID(int aufnahmeid, Connection conn) throws SQLException {
        String sql = "SELECT ROWID as aufnahmeid, * FROM Aufnahme WHERE ID = ?;";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, aufnahmeid);
        ResultSet resultSet = executeQuery(conn, hm, sql);
        return (!resultSet.next()) ? Optional.empty() : aufnahmeMapper(resultSet);
    }

    @Override
    public List<Aufnahme> getAufnahmeByPlaylistID(String playlistid, Connection conn)
            throws SQLException {
        String sql =
                "SELECT Aufnahme.ID as aufnahmeID, * FROM Aufnahme, Playlist, "
                        + " Playlist_enthält_Aufnahme\n"
                        + "WHERE Aufnahme.ID = Playlist_enthält_Aufnahme.Aufnahme\n"
                        + "AND Playlist_enthält_Aufnahme.Playlist = Playlist.ID\n"
                        + "AND Playlist.ID = ?";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, playlistid);
        ResultSet resultSet = executeQuery(conn, hm, sql);
        ArrayList<Aufnahme> aufnahmen = new ArrayList<>();
        while (resultSet.next()) {
            Optional<Aufnahme> aufnahme = aufnahmeMapper(resultSet);
            aufnahme.ifPresent(aufnahmen::add);
        }
        return aufnahmen;
    }

    private Optional<Aufnahme> aufnahmeMapper(ResultSet resultSet) throws SQLException {
        return Optional.of(
                new Aufnahme(
                        resultSet.getInt("aufnahmeid"),
                        resultSet.getString("Titel"),
                        resultSet.getInt("Dauer")));
    }
}
