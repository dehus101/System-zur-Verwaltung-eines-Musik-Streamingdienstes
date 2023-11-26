package de.hhu.cs.dbs.propra.domain.repos;

import de.hhu.cs.dbs.propra.domain.model.Playlist;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PlaylistRepo {
    List<Playlist> getPlaylists(String nameFilter, Connection conn) throws SQLException;

    Optional<Playlist> getPlaylistsByID(int playlistId, Connection conn) throws SQLException;

    Optional<Playlist> getPlaylistByExaktName(String name, Connection conn) throws SQLException;

    void insertPlaylist(String name, Boolean privat, Connection conn) throws SQLException;

    void deletePlaylist(Integer playlistid, Connection conn) throws SQLException;

    List<Playlist> getPlaylistsByPremiumNutzer(String benutzername, Connection conn)
            throws SQLException;

    void deletePremiumNutzerErstelltPlaylist(Integer playlistid, Connection conn)
            throws SQLException;

    void PlaylistInsertAufnahme(int playlistid, int aufnahmeid, Connection conn)
            throws SQLException;

    void deleteAufnahmeFromPlaylist(int playlistid, int aufnahmeid, Connection conn)
            throws SQLException;
}
