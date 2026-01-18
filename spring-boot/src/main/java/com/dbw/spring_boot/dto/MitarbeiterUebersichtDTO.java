package com.dbw.spring_boot.dto;

public record MitarbeiterUebersichtDTO(
        int personalNr,
        int anzahlVerwalteterBestellungen,
        int anzahlAngelegterProdukte
) {}
