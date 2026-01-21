package com.dbw.spring_boot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Kunde {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int kundeId;
    private String email;
    private String vorname;
    private String nachname;
    private String passwort;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<KundeAdresse> adressen;

    public int getKundeId() {
        return kundeId;
    }

    public void setKundeId(int kundeId) {
        this.kundeId = kundeId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public List<KundeAdresse> getAdressen() {
        return adressen;
    }

    public void setAdressen(List<KundeAdresse> adressen) {
        this.adressen = adressen;
    }
}
