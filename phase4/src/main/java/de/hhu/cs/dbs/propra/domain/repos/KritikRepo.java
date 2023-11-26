package de.hhu.cs.dbs.propra.domain.repos;

import de.hhu.cs.dbs.propra.domain.model.Kritik;
import de.hhu.cs.dbs.propra.domain.model.Nutzer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public interface KritikRepo {
    Optional<Kritik> insertKritik(Integer albumid, String text, Nutzer nutzer, Connection conn)
            throws SQLException;

    Optional<Kritik> getKritikByTextBenutzernameAlbum(
            String text, Nutzer nutzer, int albumid, Connection conn) throws SQLException;

    ArrayList<Kritik> getKritikenByNutzer(Nutzer nutzer, Connection conn) throws SQLException;

    void deleteKritik(Integer kritikid, Nutzer nutzer, Connection conn) throws SQLException;

    void updateKritik(
            Integer kritikid, Integer albumid, String text, Nutzer nutzer, Connection conn)
            throws SQLException;
}
