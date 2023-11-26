package de.hhu.cs.dbs.propra.domain.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.List;

public class Kuenstler {

    private final int kuenstlerid;
    private final String pseudonym;
    private final String aktiv_seit;

    @JsonUnwrapped private final PremiumNutzer premiumNutzer;

    private List<Aufnahme> aufnahmen;

    public Kuenstler(
            int kuenstlerid,
            String pseudonym,
            String aktivitaetsdatum,
            PremiumNutzer premiumNutzer,
            List<Aufnahme> aufnahmen) {
        this.kuenstlerid = kuenstlerid;
        this.pseudonym = pseudonym;
        this.premiumNutzer = premiumNutzer;
        this.aktiv_seit = aktivitaetsdatum;
        this.aufnahmen = aufnahmen;
    }

    public String getPseudonym() {
        return pseudonym;
    }

    public PremiumNutzer getPremiumNutzer() {
        return premiumNutzer;
    }

    public String getAktiv_seit() {
        return aktiv_seit;
    }

    public int getKuenstlerid() {
        return kuenstlerid;
    }

    public List<Aufnahme> getAufnahmen() {
        return aufnahmen;
    }

    public void setAufnahmen(List<Aufnahme> aufnahmen) {
        this.aufnahmen = aufnahmen;
    }
}
