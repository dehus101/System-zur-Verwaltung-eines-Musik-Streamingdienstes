package de.hhu.cs.dbs.propra.domain.model;

public class PodcastEpisode {

    private final Aufnahme aufnahme;

    private final Podcast podcast;

    private final int nummer;

    private final Sprache sprache;

    public PodcastEpisode(Aufnahme aufnahme, Podcast podcast, int nummer, Sprache sprache) {
        this.aufnahme = aufnahme;
        this.podcast = podcast;
        this.nummer = nummer;
        this.sprache = sprache;
    }

    public Aufnahme getAufnahme() {
        return aufnahme;
    }

    public Podcast getPodcast() {
        return podcast;
    }

    public int getNummer() {
        return nummer;
    }

    public Sprache getSprache() {
        return sprache;
    }
}
