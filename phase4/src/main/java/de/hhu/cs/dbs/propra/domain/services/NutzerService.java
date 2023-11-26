package de.hhu.cs.dbs.propra.domain.services;

import de.hhu.cs.dbs.propra.domain.model.Kritik;
import de.hhu.cs.dbs.propra.domain.model.Nutzer;

import java.util.ArrayList;
import java.util.Optional;

public interface NutzerService {
    Optional<Nutzer> getNutzerByBenutzername(String uniqueString);

    Optional<Kritik> insertKritik(Integer albumid, String text, Nutzer nutzer);

    ArrayList<Kritik> getKritikenByNutzer(Nutzer nutzer);

    void deleteKritik(Integer kritikid, Nutzer nutzer);

    void updateKritik(Integer kritikid, Integer albumid, String text, Nutzer nutzer);
}
