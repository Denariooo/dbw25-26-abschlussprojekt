package com.dbw.spring_boot.repository;

import com.dbw.spring_boot.dto.KundeReportDTO;
import com.dbw.spring_boot.dto.MitarbeiterBestellstatusDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ReportRepository {
    private final JdbcTemplate jdbc;

    public ReportRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<KundeReportDTO> getKundeSummeAnzahlBestellungen() {
        String sql = "SELECT kunde_id, email, anzahl_bestellungen, gesamtsumme FROM v_kunde_summe_anzahl_bestellungen";

        return jdbc.query(sql, (rs, rowNum) -> {
            KundeReportDTO dto = new KundeReportDTO();
            dto.setKundeId(rs.getInt("kunde_id"));
            dto.setEmail(rs.getString("email"));
            dto.setAnzahlBestellungen(rs.getInt("anzahl_bestellungen"));
            dto.setGesamtsumme(rs.getDouble("gesamtsumme"));
            return dto;
        });
    }

    public List<Map<String, Object>> getProduktVerkaufszahlen() {
        return jdbc.queryForList(
                "SELECT sku, name, gesamtverkaufteMenge, umsatz, anzahlBestellungen FROM v_produkt_verkaufszahlen");
    }

    public List<Map<String, Object>> getMitarbeiterUebersicht() {
        return jdbc.queryForList("SELECT personalnr AS personalNr, anzahlverwalteterbestellungen AS anzahlVerwalteterBestellungen, " +
                "anzahlangelegterprodukte AS anzahlAngelegterProdukte FROM v_mitarbeiter_uebersicht");
    }

    public List<MitarbeiterBestellstatusDTO> findMitarbeiterBestellstatusUebersicht() {
        String sql = "SELECT personalNr, status, anzahlBestellungen FROM v_mitarbeiter_bestellstatus_uebersicht";
        return jdbc.query(sql, (rs, rowNum) -> {
            MitarbeiterBestellstatusDTO dto = new MitarbeiterBestellstatusDTO();
            dto.setPersonalNr(rs.getInt("personalNr"));
            dto.setStatus(rs.getString("status"));
            dto.setAnzahlBestellungen(rs.getInt("anzahlBestellungen"));
            return dto;
        });
    }
}
