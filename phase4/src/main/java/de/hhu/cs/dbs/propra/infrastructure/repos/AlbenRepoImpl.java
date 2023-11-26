package de.hhu.cs.dbs.propra.infrastructure.repos;

import static de.hhu.cs.dbs.propra.persistence.sql.sqlite.SQLHelper.executeQuery;

import de.hhu.cs.dbs.propra.domain.model.Album;
import de.hhu.cs.dbs.propra.domain.model.Kritik;
import de.hhu.cs.dbs.propra.domain.model.Nutzer;
import de.hhu.cs.dbs.propra.domain.repos.AlbenRepo;
import de.hhu.cs.dbs.propra.domain.repos.NutzerRepo;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class AlbenRepoImpl implements AlbenRepo {

    private final NutzerRepo nutzerRepo;

    public AlbenRepoImpl(NutzerRepo nutzerRepo) {
        this.nutzerRepo = nutzerRepo;
    }

    @Override
    public List<Album> getAlben(Connection conn) throws SQLException {
        String sql = "SELECT Album.ID as albumid, * FROM Album;";
        HashMap<Integer, Object> hm = new HashMap<>();
        ResultSet resultSet = executeQuery(conn, hm, sql);
        ArrayList<Album> alben = new ArrayList<>();
        while (resultSet.next()) {
            Optional<Album> album = albenMapper(resultSet);
            album.ifPresent(alben::add);
        }
        return alben;
    }

    @Override
    public List<Kritik> getKritikenByAlbumID(int albumId, Connection conn) throws SQLException {
        String sql = "SELECT Kritik.ROWID as kritikid, * FROM Kritik WHERE Album = ?;";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, albumId);
        ResultSet resultSet = executeQuery(conn, hm, sql);
        ArrayList<Kritik> kritiken = new ArrayList<>();
        while (resultSet.next()) {
            Optional<Kritik> kritik = kritikMapper(resultSet, conn);
            kritik.ifPresent(kritiken::add);
        }
        return kritiken;
    }

    private Optional<Kritik> kritikMapper(ResultSet resultSet, Connection conn)
            throws SQLException {
        Nutzer nutzer =
                nutzerRepo
                        .getNutzerByBenutzername(resultSet.getString("Benutzername"), conn)
                        .orElse(new Nutzer(-1, "dummy", "dummy", "dummy", "dummy"));
        Album album =
                getAlbumById(resultSet.getInt("Album"), conn)
                        .orElse(new Album(-1, "dummy", -1, null));
        return Optional.of(
                new Kritik(
                        resultSet.getInt("kritikid"), resultSet.getString("Text"), nutzer, album));
    }

    @Override
    public Optional<Album> getAlbumById(int album, Connection conn) throws SQLException {
        String sql = "SELECT * FROM Album WHERE ID = ?;";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, album);
        ResultSet resultSet = executeQuery(conn, hm, sql);
        return (!resultSet.next()) ? Optional.empty() : albenMapper(resultSet);
    }

    private Optional<Album> albenMapper(ResultSet resultSet) throws SQLException {
        return Optional.of(
                new Album(
                        resultSet.getInt("ID"),
                        resultSet.getString("Bezeichnung"),
                        resultSet.getInt("Erscheinungsjahr"),
                        null));
    }
}
