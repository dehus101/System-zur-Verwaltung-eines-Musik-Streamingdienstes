package de.hhu.cs.dbs.propra.domain.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.List;

public class PremiumNutzer {

    public final int premiumnutzerid;

    @JsonUnwrapped public final Nutzer nutzer;
    public final String gueltigkeitsdauer;

    private List<Playlist> playlists;

    public PremiumNutzer(
            int premiumnutzerid,
            Nutzer nutzer,
            String gueltigkeitsdauer,
            List<Playlist> playlists) {
        this.premiumnutzerid = premiumnutzerid;
        this.nutzer = nutzer;
        this.gueltigkeitsdauer = gueltigkeitsdauer;
        this.playlists = playlists;
    }

    public Nutzer getNutzer() {
        return nutzer;
    }

    public String getGueltigkeitsdauer() {
        return gueltigkeitsdauer;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    public int getPremiumnutzerid() {
        return premiumnutzerid;
    }
}
