package de.hhu.cs.dbs.propra.infrastructure.repos;

import static de.hhu.cs.dbs.propra.persistence.sql.sqlite.SQLHelper.executeQuery;

import de.hhu.cs.dbs.propra.domain.model.Band;
import de.hhu.cs.dbs.propra.domain.repos.BandsRepo;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class BandRepoImpl implements BandsRepo {
    @Override
    public List<Band> getBands(String name, Connection conn) throws SQLException {
        String sql = "SELECT ROWID as bandid, * FROM Band WHERE Name LIKE ?;";
        HashMap<Integer, Object> hm = new HashMap<>();
        hm.put(1, name);
        ResultSet resultSet = executeQuery(conn, hm, sql);
        ArrayList<Band> bands = new ArrayList<>();
        while (resultSet.next()) {
            Optional<Band> band = bandMapper(resultSet);
            band.ifPresent(bands::add);
        }
        return bands;
    }

    private Optional<Band> bandMapper(ResultSet resultSet) throws SQLException {
        return Optional.of(
                new Band(
                        resultSet.getInt("bandid"),
                        resultSet.getString("Name"),
                        resultSet.getString("Bandgeschichte")));
    }
}
