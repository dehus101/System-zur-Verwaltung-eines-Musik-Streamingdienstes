package de.hhu.cs.dbs.propra.domain.model;

public class Genre {

    private final int genreid;

    private final String bezeichnung;

    public Genre(int genreid, String bezeichnung) {
        this.genreid = genreid;
        this.bezeichnung = bezeichnung;
    }

    public int getGenreid() {
        return genreid;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }
}
