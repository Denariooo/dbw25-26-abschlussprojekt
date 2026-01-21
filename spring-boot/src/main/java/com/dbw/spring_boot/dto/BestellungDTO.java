package com.dbw.spring_boot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.List;

public class BestellungDTO {
    private int bestellungId;
    private int kundeId;
    private int personalnr;
    private OffsetDateTime datum;
    private String status;
    private List<BestellpositionDTO> positionen;

    public int getPersonalnr() {
        return personalnr;
    }

    public void setPersonalnr(int personalnr) {
        this.personalnr = personalnr;
    }

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

    public List<BestellpositionDTO> getPositionen() {
        return positionen;
    }

    public void setPositionen(List<BestellpositionDTO> positionen) {
        this.positionen = positionen;
    }
}
