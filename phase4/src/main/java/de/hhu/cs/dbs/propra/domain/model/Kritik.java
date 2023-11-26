package de.hhu.cs.dbs.propra.domain.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonView;

import de.hhu.cs.dbs.propra.domain.views.Views;

public class Kritik {
    @JsonView(Views.IdOnly.class)
    private final int kritikid;

    @JsonView(Views.IdOnly.class)
    private final String text;

    @JsonUnwrapped
    @JsonView(Views.IdOnly.class)
    private final Nutzer nutzer;

    @JsonUnwrapped
    @JsonView(Views.IdOnly.class)
    private final Album album;

    public Kritik(int id, String text, Nutzer nutzer, Album album) {
        this.kritikid = id;
        this.text = text;
        this.nutzer = nutzer;
        this.album = album;
    }

    public int getKritikid() {
        return kritikid;
    }

    public String getText() {
        return text;
    }

    public Nutzer getNutzer() {
        return nutzer;
    }

    public Album getAlbum() {
        return album;
    }
}
