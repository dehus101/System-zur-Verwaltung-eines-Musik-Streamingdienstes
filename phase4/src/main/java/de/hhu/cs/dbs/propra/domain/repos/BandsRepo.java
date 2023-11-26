package de.hhu.cs.dbs.propra.domain.repos;

import de.hhu.cs.dbs.propra.domain.model.Band;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface BandsRepo {
    List<Band> getBands(String name, Connection conn) throws SQLException;
}
