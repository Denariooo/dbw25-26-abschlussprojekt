package com.dbw.spring_boot.repository;

import com.dbw.spring_boot.model.Produkt;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProduktRepository {

    private final JdbcTemplate jdbc;

    public ProduktRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Produkt> findAll() {
        String sql = "SELECT sku, name, preis, lagerbestand, angelegt_von FROM produkt";
        return jdbc.query(sql, (rs, rowNum) -> mapRowToProdukt(rs));
    }

    public Produkt findBySku(String sku) {
        String sql = "SELECT sku, name, preis, lagerbestand, angelegt_von FROM produkt WHERE sku = ?";
        try {
            return jdbc.queryForObject(sql, (rs, rowNum) -> mapRowToProdukt(rs), sku);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Produkt createProdukt(Produkt produkt) {
        String sql = "INSERT INTO produkt (sku, name, preis, lagerbestand, angelegt_von) VALUES (?, ?, ?, ?, ?)";
        jdbc.update(sql,
                produkt.getSku(),
                produkt.getName(),
                produkt.getPreis(),
                produkt.getLagerbestand(),
                produkt.getAngelegtVon()
        );
        return produkt;
    }

    public boolean deleteBySku(String sku) {
        String sql = "DELETE FROM produkt WHERE sku = ?";
        return jdbc.update(sql, sku) > 0;
    }

    public Produkt updateLagerbestand(String sku, int neuerLagerbestand) {
        String sql = "UPDATE produkt SET lagerbestand = ? WHERE sku = ?";
        jdbc.update(sql, neuerLagerbestand, sku);
        Produkt p = findBySku(sku);
        if (p != null) p.setLagerbestand(neuerLagerbestand);
        return p;
    }

    private Produkt mapRowToProdukt(java.sql.ResultSet rs) throws java.sql.SQLException {
        Produkt p = new Produkt();
        p.setSku(rs.getString("sku"));
        p.setName(rs.getString("name"));
        p.setPreis(rs.getDouble("preis"));
        p.setLagerbestand(rs.getInt("lagerbestand"));
        p.setAngelegtVon(rs.getInt("angelegt_von"));
        return p;
    }
}
