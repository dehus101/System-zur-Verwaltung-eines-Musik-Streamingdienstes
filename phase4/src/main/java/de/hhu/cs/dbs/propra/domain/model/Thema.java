package de.hhu.cs.dbs.propra.domain.model;

public class Thema {

    private final int id;

    private final String name;

    public Thema(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
