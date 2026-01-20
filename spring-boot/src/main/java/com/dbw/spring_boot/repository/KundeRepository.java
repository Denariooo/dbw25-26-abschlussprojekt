package com.dbw.spring_boot.repository;

import com.dbw.spring_boot.model.Kunde;
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
        String sql = "INSERT INTO kunde (email, vorname, nachname, passwort) VALUES (?, ?, ?, ?) RETURNING kunde_id";
        Integer id = jdbc.queryForObject(sql,
                new Object[]{kunde.getEmail(), kunde.getVorname(), kunde.getNachname(), kunde.getPasswort()},
                Integer.class
        );
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
        return jdbc.query(sql, new Object[]{id}, rs -> {
            if (rs.next()) {
                Kunde k = new Kunde();
                k.setKundeId(rs.getInt("kunde_id"));
                k.setEmail(rs.getString("email"));
                k.setVorname(rs.getString("vorname"));
                k.setNachname(rs.getString("nachname"));
                k.setPasswort(rs.getString("passwort"));
                k.setAdressen(new ArrayList<>());
                return k;
            }
            return null;
        });
    }

    public Kunde findByEmail(String email) {
        String sql = "SELECT * FROM kunde WHERE email = ?";
        return jdbc.query(sql, new Object[]{email}, rs -> {
            if (rs.next()) {
                Kunde k = new Kunde();
                k.setKundeId(rs.getInt("kunde_id"));
                k.setEmail(rs.getString("email"));
                k.setVorname(rs.getString("vorname"));
                k.setNachname(rs.getString("nachname"));
                k.setPasswort(rs.getString("passwort"));
                k.setAdressen(new ArrayList<>());
                return k;
            }
            return null;
        });
    }

    public Kunde findByEmailAndPasswort(String email, String passwort) {
        String sql = "SELECT * FROM kunde WHERE email = ? AND passwort = ?";
        return jdbc.query(sql, new Object[]{email, passwort}, rs -> {
            if (rs.next()) {
                Kunde k = new Kunde();
                k.setKundeId(rs.getInt("kunde_id"));
                k.setEmail(rs.getString("email"));
                k.setVorname(rs.getString("vorname"));
                k.setNachname(rs.getString("nachname"));
                k.setPasswort(rs.getString("passwort"));
                k.setAdressen(new ArrayList<>());
                return k;
            }
            return null;
        });
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
