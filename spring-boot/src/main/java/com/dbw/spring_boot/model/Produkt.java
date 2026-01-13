package com.dbw.spring_boot.model;

public class Produkt {
    private String sku;
    private String name;
    private double preis;
    private int lagerbestand;
    private int angelegtVon;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPreis() {
        return preis;
    }

    public void setPreis(double preis) {
        this.preis = preis;
    }

    public int getLagerbestand() {
        return lagerbestand;
    }

    public void setLagerbestand(int lagerbestand) {
        this.lagerbestand = lagerbestand;
    }

    public int getAngelegtVon() {
        return angelegtVon;
    }

    public void setAngelegtVon(int angelegtVon) {
        this.angelegtVon = angelegtVon;
    }
}
