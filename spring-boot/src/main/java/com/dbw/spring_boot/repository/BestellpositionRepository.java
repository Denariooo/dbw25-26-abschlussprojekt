package com.dbw.spring_boot.repository;

import com.dbw.spring_boot.model.Bestellposition;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

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
        return jdbc.query(sql, new Object[]{id}, rs -> rs.next() ? mapRow(rs) : null);
    }

    public Bestellposition createBestellposition(Bestellposition position) {
        Double preis = jdbc.queryForObject(
                "SELECT preis FROM produkt WHERE sku = ?",
                new Object[]{position.getProduktSku()},
                Double.class
        );

        double gesamtpreis = preis * position.getMenge();

        String sql = "INSERT INTO bestellposition (bestellung_id, sku, menge) VALUES (?, ?, ?) RETURNING position_id";
        Integer id = jdbc.queryForObject(sql,
                new Object[]{position.getBestellungId(), position.getProduktSku(), position.getMenge()},
                Integer.class
        );

        position.setPositionId(id);
        position.setGesamtpreis(gesamtpreis);
        return position;
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM bestellposition WHERE position_id = ?";
        jdbc.update(sql, id);
    }

    private Bestellposition mapRow(java.sql.ResultSet rs) throws java.sql.SQLException {
        Bestellposition p = new Bestellposition();
        p.setPositionId(rs.getInt("position_id"));
        p.setBestellungId(rs.getInt("bestellung_id"));
        p.setProduktSku(rs.getString("sku"));
        p.setMenge(rs.getInt("menge"));

        Double preis = jdbc.queryForObject(
                "SELECT preis FROM produkt WHERE sku = ?",
                new Object[]{p.getProduktSku()},
                Double.class
        );
        p.setGesamtpreis(preis * p.getMenge());
        return p;
    }
}
