package de.hhu.cs.dbs.propra.domain.repos;

import de.hhu.cs.dbs.propra.domain.model.Album;
import de.hhu.cs.dbs.propra.domain.model.Kritik;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface AlbenRepo {
    List<Album> getAlben(Connection conn) throws SQLException;

    List<Kritik> getKritikenByAlbumID(int albumId, Connection conn) throws SQLException;

    Optional<Album> getAlbumById(int album, Connection conn) throws SQLException;
}
