package de.hhu.cs.dbs.propra.domain.model;

public class Sprache {

    private final int id;
    private final String bezeichnung;

    public Sprache(int id, String bezeichnung) {
        this.id = id;
        this.bezeichnung = bezeichnung;
    }

    public int getId() {
        return id;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }
}
