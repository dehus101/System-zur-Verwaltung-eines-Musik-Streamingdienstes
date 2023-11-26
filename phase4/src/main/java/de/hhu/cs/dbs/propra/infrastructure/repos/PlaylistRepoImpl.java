package de.hhu.cs.dbs.propra.infrastructure.repos;

import static de.hhu.cs.dbs.propra.persistence.sql.sqlite.SQLHelper.executeQuery;
import static de.hhu.cs.dbs.propra.persistence.sql.sqlite.SQLHelper.executeUpdate;

import de.hhu.cs.dbs.propra.domain.model.Aufnahme;
import de.hhu.cs.dbs.propra.domain.model.Playlist;
import de.hhu.cs.dbs.propra.domain.repos.AufnahmeRepo;
import de.hhu.cs.dbs.propra.domain.repos.PlaylistRepo;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class PlaylistRepoImpl implements PlaylistRepo {

    private final AufnahmeRepo aufnahmeRepo;

    public PlaylistRepoImpl(AufnahmeRepo aufnahmeRepo) {
        this.aufnahmeRepo = aufnahmeRepo;
    }

    @Override
    public List<Playlist> getPlaylists(String nameFilter, Connection conn) throws SQLException {
        String sql = "SELECT ROWID as playlistid, * FROM Playlist WHERE Name LIKE ?;";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, nameFilter);
        ResultSet resultSet = executeQuery(conn, hm, sql);
        ArrayList<Playlist> playlists = new ArrayList<>();
        while (resultSet.next()) {
            Optional<Playlist> playlist = playlistMapper(resultSet, conn);
            playlist.ifPresent(playlists::add);
        }
        return playlists;
    }

    @Override
    public Optional<Playlist> getPlaylistsByID(int playlistId, Connection conn)
            throws SQLException {
        String sql = "SELECT ROWID as playlistid, * FROM Playlist WHERE ID = ?;";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, playlistId);
        ResultSet resultSet = executeQuery(conn, hm, sql);
        return (!resultSet.next()) ? Optional.empty() : playlistMapper(resultSet, conn);
    }

    @Override
    public Optional<Playlist> getPlaylistByExaktName(String name, Connection conn)
            throws SQLException {
        String sql = "SELECT ROWID as playlistid, * FROM Playlist WHERE Name = ?;";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, name);
        ResultSet resultSet = executeQuery(conn, hm, sql);
        return (!resultSet.next()) ? Optional.empty() : playlistMapper(resultSet, conn);
    }

    @Override
    public void insertPlaylist(String name, Boolean privat, Connection conn) throws SQLException {
        String sql = "INSERT INTO Playlist (Name, Sichtbarkeit) VALUES (?,?);";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, name);
        hm.put(2, privat);
        executeUpdate(conn, hm, sql);
    }

    @Override
    public void deletePlaylist(Integer playlistid, Connection conn) throws SQLException {
        String sql = "DELETE FROM Playlist WHERE ID = ?;";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, playlistid);
        executeUpdate(conn, hm, sql);
    }

    @Override
    public List<Playlist> getPlaylistsByPremiumNutzer(String benutzername, Connection conn)
            throws SQLException {
        String sql =
                "SELECT Playlist.ID as playlistid, * FROM Playlist,"
                        + " PremiumNutzer_erstellt_Playlist\n"
                        + "WHERE Playlist.ID = PremiumNutzer_erstellt_Playlist.Playlist\n"
                        + "AND PremiumNutzer_erstellt_Playlist.PremiumNutzer = ?";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, benutzername);
        ResultSet resultSet = executeQuery(conn, hm, sql);
        ArrayList<Playlist> playlists = new ArrayList<>();
        while (resultSet.next()) {
            Optional<Playlist> playlist = playlistMapper(resultSet, conn);
            playlist.ifPresent(playlists::add);
        }
        return playlists;
    }

    @Override
    public void deletePremiumNutzerErstelltPlaylist(Integer playlistid, Connection conn)
            throws SQLException {
        String sql = "DELETE FROM PremiumNutzer_erstellt_Playlist WHERE Playlist = ?;";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, playlistid);
        executeUpdate(conn, hm, sql);
    }

    @Override
    public void PlaylistInsertAufnahme(int playlistid, int aufnahmeid, Connection conn)
            throws SQLException {
        String sql = "INSERT INTO Playlist_enthält_Aufnahme (Playlist, Aufnahme) VALUES (?,?);";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, playlistid);
        hm.put(2, aufnahmeid);
        executeUpdate(conn, hm, sql);
    }

    @Override
    public void deleteAufnahmeFromPlaylist(int playlistid, int aufnahmeid, Connection conn)
            throws SQLException {
        String sql = "DELETE FROM Playlist_enthält_Aufnahme WHERE Playlist = ? AND Aufnahme = ?;";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, playlistid);
        hm.put(2, aufnahmeid);
        executeUpdate(conn, hm, sql);
    }

    private Optional<Playlist> playlistMapper(ResultSet resultSet, Connection conn)
            throws SQLException {
        List<Aufnahme> aufnahmen =
                aufnahmeRepo.getAufnahmeByPlaylistID(resultSet.getString("playlistid"), conn);
        return Optional.of(
                new Playlist(
                        resultSet.getInt("playlistid"),
                        resultSet.getString("Name"),
                        resultSet.getBoolean("Sichtbarkeit"),
                        aufnahmen));
    }
}
