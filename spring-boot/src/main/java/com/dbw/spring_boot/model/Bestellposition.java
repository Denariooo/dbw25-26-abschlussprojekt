package com.dbw.spring_boot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Bestellposition {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int positionId;

    private int bestellungId;
    private String produktSku;
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
