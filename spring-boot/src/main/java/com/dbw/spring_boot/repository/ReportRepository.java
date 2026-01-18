package com.dbw.spring_boot.repository;

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

    public List<Map<String, Object>> getKundeSummeAnzahlBestellungen() {
        return jdbc.queryForList("SELECT kunde_id, email, anzahl_bestellungen, gesamtsumme from v_kunde_summe_anzahl_bestellungen");
    }

    public List<Map<String, Object>> getProduktVerkaufszahlen() {
        return jdbc.queryForList(
                "SELECT sku, name, gesamtverkaufteMenge, umsatz, anzahlBestellungen FROM v_produkt_verkaufszahlen");
    }
}
