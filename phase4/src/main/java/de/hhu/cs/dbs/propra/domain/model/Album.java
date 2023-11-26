package de.hhu.cs.dbs.propra.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import de.hhu.cs.dbs.propra.domain.views.Views;

import java.util.List;

public class Album {
    @JsonView(Views.IdOnly.class)
    private final int albumid;

    private final String bezeichnung;

    private final int jahr;

    @JsonIgnore private final List<Kritik> kritiken;

    public Album(int albumid, String bezeichnung, int jahr, List<Kritik> kritiken) {
        this.albumid = albumid;
        this.bezeichnung = bezeichnung;
        this.jahr = jahr;
        this.kritiken = kritiken;
    }

    public int getAlbumid() {
        return albumid;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public int getJahr() {
        return jahr;
    }

    public List<Kritik> getKritiken() {
        return kritiken;
    }
}
