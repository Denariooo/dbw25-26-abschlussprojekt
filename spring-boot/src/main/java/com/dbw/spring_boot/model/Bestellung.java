package com.dbw.spring_boot.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.List;

public class Bestellung {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int bestellungId;

    private int kundeId;
    private int mitarbeiterzuweis;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private OffsetDateTime datum;

    private String status;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Bestellposition> positionen;

    public int getBestellungId() {
        return bestellungId;
    }

    public void setBestellungId(int bestellungId) {
        this.bestellungId = bestellungId;
    }

    public int getKundeId() {
        return kundeId;
    }

    public void setKundeId(int kundeId) {
        this.kundeId = kundeId;
    }

    public int getPersonalnr() {
        return mitarbeiterzuweis;
    }

    public void setPersonalnr(int mitarbeiterzuweis) {
        this.mitarbeiterzuweis = mitarbeiterzuweis;
    }

    public OffsetDateTime getDatum() {
        return datum;
    }

    public void setDatum(OffsetDateTime datum) {
        this.datum = datum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Bestellposition> getPositionen() {
        return positionen;
    }

    public void setPositionen(List<Bestellposition> positionen) {
        this.positionen = positionen;
    }
}
