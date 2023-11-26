package de.hhu.cs.dbs.propra.domain.model;

public class Podcast {

    private final int id;

    private final String name;

    public Podcast(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}
