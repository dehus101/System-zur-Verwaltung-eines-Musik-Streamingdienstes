package de.hhu.cs.dbs.propra.infrastructure.services;

import de.hhu.cs.dbs.propra.domain.errors.APIException;
import de.hhu.cs.dbs.propra.domain.model.Kritik;
import de.hhu.cs.dbs.propra.domain.model.Nutzer;
import de.hhu.cs.dbs.propra.domain.repos.KritikRepo;
import de.hhu.cs.dbs.propra.domain.repos.NutzerRepo;
import de.hhu.cs.dbs.propra.domain.services.NutzerService;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class NutzerServiceImpl implements NutzerService {

    private final SQLiteDataSource dataSource;

    private final NutzerRepo nutzerRepo;
    private final KritikRepo kritikRepo;

    public NutzerServiceImpl(
            SQLiteDataSource dataSource, NutzerRepo nutzerRepo, KritikRepo kritikRepo) {
        this.dataSource = dataSource;
        this.nutzerRepo = nutzerRepo;
        this.kritikRepo = kritikRepo;
    }

    @Override
    public Optional<Nutzer> getNutzerByBenutzername(String uniqueString) {
        try (Connection conn = dataSource.getConnection()) {
            try {
                return nutzerRepo.getNutzerByBenutzername(uniqueString, conn);
            } catch (SQLException e) {
                throw new APIException(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        } catch (SQLException e) {
            throw new APIException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Optional<Kritik> insertKritik(Integer albumid, String text, Nutzer nutzer) {
        if (albumid == null || text == null || nutzer == null) {
            throw new APIException("All fields must be provided!", HttpStatus.BAD_REQUEST);
        } else {
            try (Connection conn = dataSource.getConnection()) {
                try {
                    return kritikRepo.insertKritik(albumid, text, nutzer, conn);
                } catch (SQLException e) {
                    throw new APIException(e.getMessage(), HttpStatus.BAD_REQUEST);
                }
            } catch (SQLException e) {
                throw new APIException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @Override
    public ArrayList<Kritik> getKritikenByNutzer(Nutzer nutzer) {
        try (Connection conn = dataSource.getConnection()) {
            try {
                return kritikRepo.getKritikenByNutzer(nutzer, conn);
            } catch (SQLException e) {
                throw new APIException(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        } catch (SQLException e) {
            throw new APIException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void deleteKritik(Integer kritikid, Nutzer nutzer) {
        if (kritikid == null || nutzer == null) {
            throw new APIException("All fields must be provided!", HttpStatus.BAD_REQUEST);
        } else {
            try (Connection conn = dataSource.getConnection()) {
                try {
                    kritikRepo.deleteKritik(kritikid, nutzer, conn);
                } catch (SQLException e) {
                    throw new APIException(e.getMessage(), HttpStatus.BAD_REQUEST);
                }
            } catch (SQLException e) {
                throw new APIException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @Override
    public void updateKritik(Integer kritikid, Integer albumid, String text, Nutzer nutzer) {
        if (kritikid == null || albumid == null || text == null || nutzer == null) {
            throw new APIException("All fields must be provided!", HttpStatus.BAD_REQUEST);
        } else {
            try (Connection conn = dataSource.getConnection()) {
                try {
                    kritikRepo.updateKritik(kritikid, albumid, text, nutzer, conn);
                } catch (SQLException e) {
                    throw new APIException(e.getMessage(), HttpStatus.BAD_REQUEST);
                }
            } catch (SQLException e) {
                throw new APIException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
}
