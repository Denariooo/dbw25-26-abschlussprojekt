package com.dbw.spring_boot.repository;

import com.dbw.spring_boot.model.Mitarbeiter;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MitarbeiterRepository {

    private final JdbcTemplate jdbcTemplate;

    public MitarbeiterRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Mitarbeiter> findAll() {
        return jdbcTemplate.query("SELECT * FROM mitarbeiter",
                (rs, rowNum) -> {
                    Mitarbeiter m = new Mitarbeiter();
                    m.setPersonalNr(rs.getInt("personal_nr"));
                    m.setVorname(rs.getString("vorname"));
                    m.setNachname(rs.getString("nachname"));
                    m.setEmail(rs.getString("email"));
                    m.setPasswort(rs.getString("passwort"));
                    return m;
                });
    }

    public Mitarbeiter findById(int id) {
        String sql = "SELECT * FROM mitarbeiter WHERE personal_nr = ?";
        List<Mitarbeiter> list = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Mitarbeiter m = new Mitarbeiter();
            m.setPersonalNr(rs.getInt("personal_nr"));
            m.setVorname(rs.getString("vorname"));
            m.setNachname(rs.getString("nachname"));
            m.setEmail(rs.getString("email"));
            m.setPasswort(rs.getString("passwort"));
            return m;
        }, id);

        return DataAccessUtils.singleResult(list);
    }

    public Mitarbeiter save(Mitarbeiter m) {
        String sql = "INSERT INTO mitarbeiter (vorname, nachname, email, passwort) VALUES (?, ?, ?, ?) RETURNING personal_nr";
        List<Integer> ids = jdbcTemplate.query(
                sql,
                new Object[]{m.getVorname(), m.getNachname(), m.getEmail(), m.getPasswort()},
                (rs, rowNum) -> rs.getInt("personal_nr")
        );
        Integer id = DataAccessUtils.singleResult(ids);
        if (id == null) throw new EmptyResultDataAccessException("Insert failed, no ID returned", 1);
        m.setPersonalNr(id);
        return m;
    }

    public int deleteById(int id) {
        return jdbcTemplate.update(
                "DELETE FROM mitarbeiter WHERE personal_nr = ?", id
        );
    }

    public Mitarbeiter findByPersonalNrAndPasswort(int personalNr, String passwort) {
        String sql = "SELECT * FROM mitarbeiter WHERE personal_nr = ? AND passwort = ?";
        List<Mitarbeiter> list = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Mitarbeiter m = new Mitarbeiter();
            m.setPersonalNr(rs.getInt("personal_nr"));
            m.setVorname(rs.getString("vorname"));
            m.setNachname(rs.getString("nachname"));
            m.setEmail(rs.getString("email"));
            m.setPasswort(rs.getString("passwort"));
            return m;
        }, personalNr, passwort);
        return DataAccessUtils.singleResult(list);
    }
}
