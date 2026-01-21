package com.dbw.spring_boot.dto;

public class BestellpositionDTO {
    private int positionId;
    private int bestellungId;
    private ProduktDTO produkt;
    private int menge;
    private double gesamtpreis;

    public int getPositionId() {
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    public int getBestellungId() {
        return bestellungId;
    }

    public void setBestellungId(int bestellungId) {
        this.bestellungId = bestellungId;
    }

    public ProduktDTO getProdukt() {
        return produkt;
    }

    public void setProdukt(ProduktDTO produkt) {
        this.produkt = produkt;
    }

    public int getMenge() {
        return menge;
    }

    public void setMenge(int menge) {
        this.menge = menge;
    }

    public double getGesamtpreis() {
        return gesamtpreis;
    }

    public void setGesamtpreis(double gesamtpreis) {
        this.gesamtpreis = gesamtpreis;
    }
}
