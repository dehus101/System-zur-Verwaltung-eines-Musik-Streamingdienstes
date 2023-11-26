package de.hhu.cs.dbs.propra.infrastructure.repos;

import static de.hhu.cs.dbs.propra.persistence.sql.sqlite.SQLHelper.executeQuery;
import static de.hhu.cs.dbs.propra.persistence.sql.sqlite.SQLHelper.executeUpdate;

import de.hhu.cs.dbs.propra.domain.model.Album;
import de.hhu.cs.dbs.propra.domain.model.Kritik;
import de.hhu.cs.dbs.propra.domain.model.Nutzer;
import de.hhu.cs.dbs.propra.domain.repos.AlbenRepo;
import de.hhu.cs.dbs.propra.domain.repos.KritikRepo;
import de.hhu.cs.dbs.propra.domain.repos.NutzerRepo;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

@Repository
public class KritikRepoImpl implements KritikRepo {

    private final NutzerRepo nutzerRepo;
    private final AlbenRepo albenRepo;

    public KritikRepoImpl(NutzerRepo nutzerRepo, AlbenRepo albenRepo) {
        this.nutzerRepo = nutzerRepo;
        this.albenRepo = albenRepo;
    }

    @Override
    public Optional<Kritik> insertKritik(
            Integer albumid, String text, Nutzer nutzer, Connection conn) throws SQLException {
        String sql = "INSERT INTO Kritik (Text, Benutzername, Album) VALUES (?, ?, ?)";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, text);
        hm.put(2, nutzer.getBenutzername());
        hm.put(3, albumid);
        executeUpdate(conn, hm, sql);
        return getKritikByTextBenutzernameAlbum(text, nutzer, albumid, conn);
    }

    @Override
    public Optional<Kritik> getKritikByTextBenutzernameAlbum(
            String text, Nutzer nutzer, int albumid, Connection conn) throws SQLException {
        String sql = "SELECT * FROM kritik WHERE Text = ? AND Benutzername = ? AND Album = ?";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, text);
        hm.put(2, nutzer.getBenutzername());
        hm.put(3, albumid);
        ResultSet resultSet = executeQuery(conn, hm, sql);
        return (!resultSet.next()) ? Optional.empty() : kritikMapper(resultSet, conn);
    }

    @Override
    public ArrayList<Kritik> getKritikenByNutzer(Nutzer nutzer, Connection conn)
            throws SQLException {
        String sql = "SELECT * FROM kritik WHERE Benutzername = ?";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, nutzer.getBenutzername());
        ResultSet resultSet = executeQuery(conn, hm, sql);
        ArrayList<Kritik> kritiken = new ArrayList<>();
        while (resultSet.next()) {
            kritiken.add(kritikMapper(resultSet, conn).orElseThrow());
        }
        return kritiken;
    }

    @Override
    public void deleteKritik(Integer kritikid, Nutzer nutzer, Connection conn) throws SQLException {
        String sql = "DELETE FROM kritik WHERE ID = ? AND Benutzername = ?";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, kritikid);
        hm.put(2, nutzer.getBenutzername());
        executeUpdate(conn, hm, sql);
    }

    @Override
    public void updateKritik(
            Integer kritikid, Integer albumid, String text, Nutzer nutzer, Connection conn)
            throws SQLException {
        String sql = "UPDATE kritik SET Album = ?, Text = ? WHERE ID = ? AND Benutzername = ?";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, albumid);
        hm.put(2, text);
        hm.put(3, kritikid);
        hm.put(4, nutzer.getBenutzername());
        executeUpdate(conn, hm, sql);
    }

    private Optional<Kritik> kritikMapper(ResultSet resultSet, Connection conn)
            throws SQLException {
        Nutzer nutzer =
                nutzerRepo
                        .getNutzerByBenutzername(resultSet.getString("Benutzername"), conn)
                        .orElseThrow();
        Album album = albenRepo.getAlbumById(resultSet.getInt("Album"), conn).orElseThrow();
        return Optional.of(
                new Kritik(resultSet.getInt("ID"), resultSet.getString("Text"), nutzer, album));
    }
}
