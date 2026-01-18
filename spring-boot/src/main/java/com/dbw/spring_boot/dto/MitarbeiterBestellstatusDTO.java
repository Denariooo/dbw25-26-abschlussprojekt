package com.dbw.spring_boot.dto;

public class MitarbeiterBestellstatusDTO {
    private int personalNr;
    private String status;
    private int anzahlBestellungen;

    public int getPersonalNr() {
        return personalNr;
    }

    public void setPersonalNr(int personalNr) {
        this.personalNr = personalNr;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAnzahlBestellungen() {
        return anzahlBestellungen;
    }

    public void setAnzahlBestellungen(int anzahlBestellungen) {
        this.anzahlBestellungen = anzahlBestellungen;
    }
}
