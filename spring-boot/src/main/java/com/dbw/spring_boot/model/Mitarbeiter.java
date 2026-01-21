package com.dbw.spring_boot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Mitarbeiter {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer personalNr;

    private String passwort;
    private String email;
    private String vorname;
    private String nachname;

    public Integer getPersonalNr() {
        return personalNr;
    }

    public void setPersonalNr(Integer personalNr) {
        this.personalNr = personalNr;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }
}
