package com.dbw.spring_boot.dto;

public class LoginRequestDTO {

    private int personalNr;
    private String passwort;

    public int getPersonalNr() {
        return personalNr;
    }

    public void setPersonalNr(int personalNr) {
        this.personalNr = personalNr;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }
}
