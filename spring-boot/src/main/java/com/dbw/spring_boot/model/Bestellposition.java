package com.dbw.spring_boot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Bestellposition {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int positionsId;
    private int bestellungId;
    private String produktSku;
    private int menge;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)

    private double gesamtpreis;

    public int getPositionsId() {
        return positionsId;
    }

    public void setPositionsId(int positionsId) {
        this.positionsId = positionsId;
    }

    public int getBestellungId() {
        return bestellungId;
    }

    public void setBestellungId(int bestellungId) {
        this.bestellungId = bestellungId;
    }

    public String getProduktSku() {
        return produktSku;
    }

    public void setProduktSku(String produktSku) {
        this.produktSku = produktSku;
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
