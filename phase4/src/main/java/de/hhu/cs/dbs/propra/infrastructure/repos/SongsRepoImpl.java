package de.hhu.cs.dbs.propra.infrastructure.repos;

import static de.hhu.cs.dbs.propra.persistence.sql.sqlite.SQLHelper.executeQuery;
import static de.hhu.cs.dbs.propra.persistence.sql.sqlite.SQLHelper.executeUpdate;

import de.hhu.cs.dbs.propra.domain.model.Aufnahme;
import de.hhu.cs.dbs.propra.domain.model.Genre;
import de.hhu.cs.dbs.propra.domain.model.Song;
import de.hhu.cs.dbs.propra.domain.repos.SongRepo;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class SongsRepoImpl implements SongRepo {

    @Override
    public List<Song> getSongs(Connection conn) throws SQLException {
        String sql =
                "SELECT Song.ID as songid, Aufnahme.ID as aufnahmeid, * FROM Song, Aufnahme WHERE"
                        + " Song.ID = Aufnahme.ID;";
        HashMap<Integer, Object> hm = new HashMap<>();
        ResultSet resultSet = executeQuery(conn, hm, sql);
        ArrayList<Song> songs = new ArrayList<>();
        while (resultSet.next()) {
            Optional<Song> song = songMapper(resultSet);
            song.ifPresent(songs::add);
        }
        return songs;
    }

    @Override
    public Optional<Song> getSongByTitel(String titel, Connection conn) throws SQLException {
        String sql =
                "SELECT Song.ID as songid, Aufnahme.ID as aufnahmeid, Aufnahme.Titel as titel,"
                        + " Aufnahme.Dauer as dauer, Song.SpeicherortHQ as SpeicherortHQ,"
                        + " Song.SpeicherortLQ as SpeicherortLQ\n"
                        + "FROM Song INNER JOIN Aufnahme ON Song.ID = Aufnahme.ID\n"
                        + "\n"
                        + "WHERE Aufnahme.Titel LIKE ?";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, titel);
        ResultSet resultSet = executeQuery(conn, hm, sql);
        return (!resultSet.next()) ? Optional.empty() : songMapper(resultSet);
    }

    @Override
    public void insertSong(String speicherortHq, String speicherortLq, int genre, Connection conn)
            throws SQLException {
        String sql = "INSERT INTO Song (SpeicherortLQ, SpeicherortHQ, Genre) VALUES (?,?,?);";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, speicherortHq);
        hm.put(2, speicherortLq);
        hm.put(3, genre);
        executeUpdate(conn, hm, sql);
    }

    @Override
    public Genre getDummyGenre(Connection conn) throws SQLException {
        String sql = "SELECT * FROM Genre WHERE ID = 0;";
        HashMap<Integer, Object> hm = new HashMap<>();
        ResultSet resultSet = executeQuery(conn, hm, sql);
        return (!resultSet.next()) ? createDummyGenre(conn) : new Genre(0, "Default");
    }

    @Override
    public Genre createDummyGenre(Connection conn) throws SQLException {
        String sql = "INSERT INTO Genre (ID, Bezeichnung) VALUES (?,?);";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, 0);
        hm.put(2, "Default");
        executeUpdate(conn, hm, sql);
        return new Genre(0, "Default");
    }

    @Override
    public void deleteSong(Integer songid, Connection conn) throws SQLException {
        String sql = "DELETE FROM Song WHERE ID = ?;";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, songid);
        executeUpdate(conn, hm, sql);
    }

    @Override
    public Optional<Song> getSongById(Integer songid, Connection conn) throws SQLException {
        String sql =
                "SELECT Song.ID as songid, Aufnahme.ID as aufnahmeid, Aufnahme.Titel as titel,"
                        + " Aufnahme.Dauer as dauer, Song.SpeicherortHQ as SpeicherortHQ,"
                        + " Song.SpeicherortLQ as SpeicherortLQ\n"
                        + "FROM Song INNER JOIN Aufnahme ON Song.ID = Aufnahme.ID\n"
                        + "\n"
                        + "WHERE Song.ID = ?";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, songid);
        ResultSet resultSet = executeQuery(conn, hm, sql);
        return (!resultSet.next()) ? Optional.empty() : songMapper(resultSet);
    }

    @Override
    public void insertAufnahmeSong(int songid, Genre dummyGenre, Connection conn)
            throws SQLException {
        String sql =
                "INSERT INTO Song (ID, SpeicherortLQ, SpeicherortHQ, Genre ) VALUES (?,"
                        + " '/PlaylistAufnahmen/LQ', '/PlaylistAufnahmen/HQ', ?);";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, songid);
        hm.put(2, dummyGenre);
        executeUpdate(conn, hm, sql);
    }

    private Optional<Song> songMapper(ResultSet resultSet) throws SQLException {
        Aufnahme aufnahme =
                new Aufnahme(
                        resultSet.getInt("aufnahmeid"),
                        resultSet.getString("titel"),
                        resultSet.getInt("dauer"));
        return Optional.of(
                new Song(
                        resultSet.getInt("songid"),
                        aufnahme,
                        resultSet.getString("SpeicherortLQ"),
                        resultSet.getString("SpeicherortHQ"),
                        null));
    }
}
