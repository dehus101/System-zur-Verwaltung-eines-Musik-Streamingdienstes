package de.hhu.cs.dbs.propra.domain.model;

public class Podcaster {

    private final int podcasterId;
    private final Kuenstler kuenstler;
    private final boolean level;

    public Podcaster(int podcasterId, Kuenstler kuenstler, boolean level) {
        this.podcasterId = podcasterId;
        this.kuenstler = kuenstler;
        this.level = level;
    }

    public int getPodcasterId() {
        return podcasterId;
    }

    public Kuenstler getKuenstler() {
        return kuenstler;
    }

    public boolean isLevel() {
        return level;
    }
}
