package de.hhu.cs.dbs.propra.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class Song {
    private final int songid;

    @JsonUnwrapped private final Aufnahme aufnahme;

    private final String speicherort_hq;

    private final String speicherort_lq;

    @JsonIgnore private final Genre genre;

    public Song(
            int songid,
            Aufnahme aufnahme,
            String speicherort_hq,
            String speicherort_lq,
            Genre genre) {
        this.songid = songid;
        this.aufnahme = aufnahme;
        this.speicherort_hq = speicherort_hq;
        this.speicherort_lq = speicherort_lq;
        this.genre = genre;
    }

    public int getSongid() {
        return songid;
    }

    public Aufnahme getAufnahme() {
        return aufnahme;
    }

    public String getSpeicherort_hq() {
        return speicherort_hq;
    }

    public String getSpeicherort_lq() {
        return speicherort_lq;
    }

    public Genre getGenre() {
        return genre;
    }
}
