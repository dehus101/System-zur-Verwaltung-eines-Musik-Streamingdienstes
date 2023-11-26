package de.hhu.cs.dbs.propra.domain.model;

public class Band {

    private final int bandid;

    private final String name;

    private final String geschichte;

    public Band(int bandId, String name, String bandgeschichte) {
        this.bandid = bandId;
        this.name = name;
        this.geschichte = bandgeschichte;
    }

    public int getBandid() {
        return bandid;
    }

    public String getName() {
        return name;
    }

    public String getGeschichte() {
        return geschichte;
    }
}
