package de.hhu.cs.dbs.propra.domain.model;

import com.fasterxml.jackson.annotation.JsonView;

import de.hhu.cs.dbs.propra.domain.views.Views;

public class Nutzer {
    @JsonView(Views.IdOnly.class)
    private final int nutzerid;

    private final String benutzername;

    private final String email;
    private final String password;
    private final String iban;

    public Nutzer(int nutzerId, String benutzername, String email, String password, String iban) {
        this.nutzerid = nutzerId;
        this.benutzername = benutzername;
        this.email = email;
        this.password = password;
        this.iban = iban;
    }

    public int getNutzerid() {
        return nutzerid;
    }

    public String getBenutzername() {
        return benutzername;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getIban() {
        return iban;
    }
}
