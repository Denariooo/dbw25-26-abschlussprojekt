package com.dbw.spring_boot.dto;

public class KundeReportDTO {
    private int kundeId;
    private String email;
    private int anzahlBestellungen;
    private double gesamtsumme;

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

    public int getAnzahlBestellungen() {
        return anzahlBestellungen;
    }

    public void setAnzahlBestellungen(int anzahlBestellungen) {
        this.anzahlBestellungen = anzahlBestellungen;
    }

    public double getGesamtsumme() {
        return gesamtsumme;
    }

    public void setGesamtsumme(double gesamtsumme) {
        this.gesamtsumme = gesamtsumme;
    }
}
