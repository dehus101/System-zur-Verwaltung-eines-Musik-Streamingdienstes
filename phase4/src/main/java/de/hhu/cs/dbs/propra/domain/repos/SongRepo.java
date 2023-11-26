package de.hhu.cs.dbs.propra.domain.repos;

import de.hhu.cs.dbs.propra.domain.model.Genre;
import de.hhu.cs.dbs.propra.domain.model.Song;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface SongRepo {
    List<Song> getSongs(Connection conn) throws SQLException;

    Optional<Song> getSongByTitel(String titel, Connection conn) throws SQLException;

    void insertSong(String speicherortHq, String speicherortLq, int genre, Connection conn)
            throws SQLException;

    Genre getDummyGenre(Connection conn) throws SQLException;

    Genre createDummyGenre(Connection conn) throws SQLException;

    void deleteSong(Integer songid, Connection conn) throws SQLException;

    Optional<Song> getSongById(Integer songid, Connection conn) throws SQLException;

    void insertAufnahmeSong(int songid, Genre dummyGenre, Connection conn) throws SQLException;
}
