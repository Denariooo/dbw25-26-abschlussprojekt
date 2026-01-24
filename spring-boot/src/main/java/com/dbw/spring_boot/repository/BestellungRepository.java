package com.dbw.spring_boot.repository;

import com.dbw.spring_boot.model.Bestellung;
import com.dbw.spring_boot.model.Bestellposition;
import com.dbw.spring_boot.model.Produkt;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BestellungRepository {

    private final JdbcTemplate jdbc;

    public BestellungRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Bestellung> findAll() {
        String sql = "SELECT * FROM bestellung";
        return jdbc.query(sql, (rs, rowNum) -> {
            Bestellung b = new Bestellung();
            b.setBestellungId(rs.getInt("bestellung_id"));
            b.setKundeId(rs.getInt("kunde_id"));
            b.setPersonalnr(rs.getInt("mitarbeiterzuweis"));
            b.setStatus(rs.getString("status"));
            b.setDatum(rs.getObject("datum", OffsetDateTime.class));
            b.setPositionen(findPositionenByBestellungId(b.getBestellungId()));
            return b;
        });
    }

    public Bestellung findById(int id) {
        String sql = "SELECT * FROM bestellung WHERE bestellung_id = ?";
        return jdbc.queryForObject(sql, (rs, rowNum) -> {
            Bestellung b = new Bestellung();
            b.setBestellungId(rs.getInt("bestellung_id"));
            b.setKundeId(rs.getInt("kunde_id"));
            b.setPersonalnr(rs.getInt("mitarbeiterzuweis"));
            b.setStatus(rs.getString("status"));
            b.setDatum(rs.getObject("datum", OffsetDateTime.class));
            b.setPositionen(findPositionenByBestellungId(id));
            return b;
        }, id);
    }

    private List<Bestellposition> findPositionenByBestellungId(int bestellungId) {
        String sql = """
        SELECT bp.position_id, bp.bestellung_id, bp.sku, bp.menge,
               (bp.menge * p.preis) AS gesamtpreis,
               p.name, p.preis, p.lagerbestand, p.angelegt_von
        FROM bestellposition bp
        JOIN produkt p ON bp.sku = p.sku
        WHERE bp.bestellung_id = ?
    """;
        return jdbc.query(sql, (rs, rowNum) -> {
            Produkt p = new Produkt();
            p.setSku(rs.getString("sku"));
            p.setName(rs.getString("name"));
            p.setPreis(rs.getDouble("preis"));
            p.setLagerbestand(rs.getInt("lagerbestand"));
            p.setAngelegtVon(rs.getInt("angelegt_von"));

            Bestellposition bp = new Bestellposition();
            bp.setPositionsId(rs.getInt("position_id"));
            bp.setBestellungId(rs.getInt("bestellung_id"));
            bp.setProduktSku(rs.getString("sku"));
            bp.setMenge(rs.getInt("menge"));
            bp.setGesamtpreis(rs.getDouble("gesamtpreis"));
            return bp;
        }, bestellungId);
    }


    public Bestellung createBestellung(Bestellung bestellung) {
        String sql = """
                    INSERT INTO bestellung (kunde_id, mitarbeiterzuweis, datum, status)
                    VALUES (?, ?, ?, ?)
                    RETURNING bestellung_id
                """;

        Integer id = jdbc.queryForObject(
                sql,
                Integer.class,
                bestellung.getKundeId(),
                bestellung.getPersonalnr(),
                bestellung.getDatum(),
                bestellung.getStatus()
        );
        if (id == null) throw new IllegalStateException("Bestellung konnte nicht erstellt werden");
        bestellung.setBestellungId(id);
        bestellung.setPositionen(new ArrayList<>());
        return bestellung;
    }

    public boolean deleteBestellung(int id) {
        int rows = jdbc.update(
                "DELETE FROM bestellung WHERE bestellung_id = ?", id
        );
        return rows > 0;
    }
}
