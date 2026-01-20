package com.dbw.spring_boot.repository;

import com.dbw.spring_boot.model.Mitarbeiter;
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
        return jdbcTemplate.queryForObject(
                "SELECT * FROM mitarbeiter WHERE personal_nr = ?",
                new Object[]{id},
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

    public void save(Mitarbeiter m) {
        jdbcTemplate.update(
                "INSERT INTO mitarbeiter (vorname, nachname, email, passwort) VALUES (?, ?, ?, ?)",
                m.getVorname(), m.getNachname(), m.getEmail(), m.getPasswort()
        );
    }

    public void deleteById(int id) {
        jdbcTemplate.update("DELETE FROM mitarbeiter WHERE personal_nr = ?", id);
    }

    public Mitarbeiter login(Integer personalNr, String passwort) {

        String sql = """
                    SELECT personal_nr, email, vorname, nachname
                    FROM mitarbeiter
                    WHERE personal_nr = ? AND passwort = ?
                """;

        List<Mitarbeiter> result = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> {
                    Mitarbeiter m = new Mitarbeiter();
                    m.setPersonalNr(rs.getInt("personal_nr"));
                    m.setEmail(rs.getString("email"));
                    m.setVorname(rs.getString("vorname"));
                    m.setNachname(rs.getString("nachname"));
                    m.setPasswort(null);
                    return m;
                },
                personalNr,
                passwort
        );

        return result.isEmpty() ? null : result.get(0);
    }

    public Mitarbeiter findByPersonalNrAndPasswort(int personalNr, String passwort) {
        String sql = "SELECT * FROM mitarbeiter WHERE personal_nr = ? AND passwort = ?";
        List<Mitarbeiter> list = jdbcTemplate.query(
                sql,
                new Object[]{personalNr, passwort},
                (rs, rowNum) -> {
                    Mitarbeiter m = new Mitarbeiter();
                    m.setPersonalNr(rs.getInt("personal_nr"));
                    m.setVorname(rs.getString("vorname"));
                    m.setNachname(rs.getString("nachname"));
                    m.setEmail(rs.getString("email"));
                    m.setPasswort(rs.getString("passwort"));
                    return m;
                }
        );
        return list.isEmpty() ? null : list.get(0);
    }

}
