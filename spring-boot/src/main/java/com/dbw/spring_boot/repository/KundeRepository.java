package com.dbw.spring_boot.repository;

import com.dbw.spring_boot.model.Kunde;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class KundeRepository {

    private final JdbcTemplate jdbc;

    public KundeRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Kunde createKunde(Kunde kunde) {
        List<Integer> ids = jdbc.query(
                "INSERT INTO kunde (email, vorname, nachname, passwort) VALUES (?, ?, ?, ?) RETURNING kunde_id",
                (rs, rowNum) -> rs.getInt("kunde_id"),
                kunde.getEmail(),
                kunde.getVorname(),
                kunde.getNachname(),
                kunde.getPasswort()
        );

        Integer id = DataAccessUtils.singleResult(ids);
        if (id == null) throw new IllegalStateException("Fehler beim Einfügen des Kunden.");
        kunde.setKundeId(id);
        kunde.setAdressen(new ArrayList<>());
        return kunde;
    }

    public Kunde updateKunde(int id, Kunde kunde) {
        String sql = "UPDATE kunde SET email = ?, vorname = ?, nachname = ?, passwort = ? WHERE kunde_id = ?";
        jdbc.update(sql,
                kunde.getEmail(),
                kunde.getVorname(),
                kunde.getNachname(),
                kunde.getPasswort(),
                id
        );
        return findById(id);
    }

    public Kunde findById(int id) {
        String sql = "SELECT * FROM kunde WHERE kunde_id = ?";
        try {
            return jdbc.queryForObject(sql, (rs, rowNum) -> {
                Kunde k = new Kunde();
                k.setKundeId(rs.getInt("kunde_id"));
                k.setEmail(rs.getString("email"));
                k.setVorname(rs.getString("vorname"));
                k.setNachname(rs.getString("nachname"));
                k.setPasswort(rs.getString("passwort"));
                k.setAdressen(new ArrayList<>());
                return k;
            }, id);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Kunde findByEmail(String email) {
        String sql = "SELECT * FROM kunde WHERE email = ?";
        try {
            return jdbc.queryForObject(sql, (rs, rowNum) -> {
                Kunde k = new Kunde();
                k.setKundeId(rs.getInt("kunde_id"));
                k.setEmail(rs.getString("email"));
                k.setVorname(rs.getString("vorname"));
                k.setNachname(rs.getString("nachname"));
                k.setPasswort(rs.getString("passwort"));
                k.setAdressen(new ArrayList<>());
                return k;
            }, email);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Kunde findByEmailAndPasswort(String email, String passwort) {
        String sql = "SELECT * FROM kunde WHERE email = ? AND passwort = ?";
        List<Kunde> list = jdbc.query(sql, (rs, rowNum) -> {
            Kunde k = new Kunde();
            k.setKundeId(rs.getInt("kunde_id"));
            k.setEmail(rs.getString("email"));
            k.setVorname(rs.getString("vorname"));
            k.setNachname(rs.getString("nachname"));
            k.setPasswort(rs.getString("passwort"));
            k.setAdressen(new ArrayList<>());
            return k;
        }, email, passwort);
        return DataAccessUtils.singleResult(list);
    }

    public List<Kunde> findAll() {
        String sql = "SELECT * FROM kunde";
        return jdbc.query(sql, (rs, rowNum) -> {
            Kunde k = new Kunde();
            k.setKundeId(rs.getInt("kunde_id"));
            k.setEmail(rs.getString("email"));
            k.setVorname(rs.getString("vorname"));
            k.setNachname(rs.getString("nachname"));
            k.setPasswort(rs.getString("passwort"));
            k.setAdressen(new ArrayList<>());
            return k;
        });
    }
}
