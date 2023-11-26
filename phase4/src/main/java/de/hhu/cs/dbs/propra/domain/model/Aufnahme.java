package de.hhu.cs.dbs.propra.domain.model;

public class Aufnahme {

    private final int aufnahmeid;

    private final String titel;

    private final int dauer;

    public Aufnahme(int aufnahmeid, String titel, int dauer) {
        this.aufnahmeid = aufnahmeid;
        this.titel = titel;
        this.dauer = dauer;
    }

    public int getAufnahmeid() {
        return aufnahmeid;
    }

    public String getTitel() {
        return titel;
    }

    public int getDauer() {
        return dauer;
    }
}
