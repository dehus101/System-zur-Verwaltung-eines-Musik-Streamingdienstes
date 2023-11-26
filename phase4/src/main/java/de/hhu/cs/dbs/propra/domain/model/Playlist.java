package de.hhu.cs.dbs.propra.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class Playlist {

    private final int playlistid;

    private final String name;

    private final boolean sichtbarkeit;

    @JsonIgnore private final List<Aufnahme> aufnahmen;

    public Playlist(int id, String name, boolean sichtbarkeit, List<Aufnahme> aufnahmen) {
        this.playlistid = id;
        this.name = name;
        this.sichtbarkeit = sichtbarkeit;
        this.aufnahmen = aufnahmen;
    }

    public int getPlaylistid() {
        return playlistid;
    }

    public String getName() {
        return name;
    }

    public boolean getSichtbarkeit() {
        return sichtbarkeit;
    }

    public List<Aufnahme> getAufnahmen() {
        return aufnahmen;
    }
}
