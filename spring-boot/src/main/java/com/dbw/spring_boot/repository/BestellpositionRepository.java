package com.dbw.spring_boot.repository;

import com.dbw.spring_boot.model.Bestellposition;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.dao.support.DataAccessUtils.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Repository
public class BestellpositionRepository {

    private final JdbcTemplate jdbc;

    public BestellpositionRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Bestellposition> findAll() {
        String sql = "SELECT position_id, bestellung_id, sku, menge FROM bestellposition";
        return jdbc.query(sql, (rs, rowNum) -> mapRow(rs));
    }

    public Bestellposition findById(int id) {
        String sql = "SELECT position_id, bestellung_id, sku, menge FROM bestellposition WHERE position_id = ?";
        try {
            return jdbc.queryForObject(sql, (rs, rowNum) -> mapRow(rs), id);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Bestellposition createBestellposition(Bestellposition position) {
        List<Double> preise = jdbc.query(
                "SELECT preis FROM produkt WHERE sku = ?",
                (rs, rowNum) -> rs.getDouble("preis"),
                position.getProduktSku()
        );
        Double preis = singleResult(preise);
        if (preis == null) throw new IllegalArgumentException("Produkt mit SKU " + position.getProduktSku() + " nicht gefunden.");
        double gesamtpreis = preis * position.getMenge();
        List<Integer> ids = jdbc.query(
                "INSERT INTO bestellposition (bestellung_id, sku, menge) VALUES (?, ?, ?) RETURNING position_id",
                (rs, rowNum) -> rs.getInt("position_id"),
                position.getBestellungId(),
                position.getProduktSku(),
                position.getMenge()
        );
        Integer id = singleResult(ids);
        if (id == null) throw new IllegalStateException("Fehler beim Einfügen der Bestellposition.");
        position.setPositionId(id);
        position.setGesamtpreis(gesamtpreis);
        return position;
    }

    public boolean deleteById(int id) {
        int affected = jdbc.update(
                "DELETE FROM bestellposition WHERE position_id = ?",
                id
        );
        return affected > 0;
    }

    private Bestellposition mapRow(java.sql.ResultSet rs) throws java.sql.SQLException {
        Bestellposition p = new Bestellposition();
        p.setPositionId(rs.getInt("position_id"));
        p.setBestellungId(rs.getInt("bestellung_id"));
        p.setProduktSku(rs.getString("sku"));
        p.setMenge(rs.getInt("menge"));

        List<Double> preise = jdbc.query(
                "SELECT preis FROM produkt WHERE sku = ?",
                (rs2, rowNum) -> rs2.getDouble("preis"),
                p.getProduktSku()
        );
        Double preis = singleResult(preise);
        if (preis == null) throw new IllegalStateException("Produkt mit SKU " + p.getProduktSku() + " nicht gefunden.");
        p.setGesamtpreis(preis * p.getMenge());
        return p;
    }
}
