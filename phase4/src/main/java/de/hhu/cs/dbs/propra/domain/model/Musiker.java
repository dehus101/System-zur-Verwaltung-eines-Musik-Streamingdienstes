package de.hhu.cs.dbs.propra.domain.model;

public class Musiker {

    private final Kuenstler kuenstler;

    private final boolean auftrittsfaehig;

    public Musiker(Kuenstler kuenstler, boolean auftrittsfaehig) {
        this.kuenstler = kuenstler;
        this.auftrittsfaehig = auftrittsfaehig;
    }

    public Kuenstler getKuenstler() {
        return kuenstler;
    }

    public boolean getAuftrittsfaehig() {
        return auftrittsfaehig;
    }
}
