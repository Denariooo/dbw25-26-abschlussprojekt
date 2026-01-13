package com.dbw.spring_boot.repository;

import com.dbw.spring_boot.model.Adresse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AdresseRepository {

    private final JdbcTemplate jdbc;

    public AdresseRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Adresse> findAll() {
        String sql = "SELECT adresse_id, aktiv, strasse, hausnummer, plz, ort, land FROM adresse";
        return jdbc.query(sql, (rs, rowNum) -> {
            Adresse a = new Adresse();
            a.setAdresseId(rs.getInt("adresse_id"));
            a.setAktiv(rs.getBoolean("aktiv"));
            a.setStrasse(rs.getString("strasse"));
            a.setHausnummer(rs.getString("hausnummer"));
            a.setPlz(rs.getInt("plz"));
            a.setOrt(rs.getString("ort"));
            a.setLand(rs.getString("land"));
            return a;
        });
    }

    public Adresse findById(int id) {
        String sql = "SELECT adresse_id, aktiv, strasse, hausnummer, plz, ort, land FROM adresse WHERE adresse_id = ?";
        List<Adresse> list = jdbc.query(sql, new Object[]{id}, (rs, rowNum) -> {
            Adresse a = new Adresse();
            a.setAdresseId(rs.getInt("adresse_id"));
            a.setAktiv(rs.getBoolean("aktiv"));
            a.setStrasse(rs.getString("strasse"));
            a.setHausnummer(rs.getString("hausnummer"));
            a.setPlz(rs.getInt("plz"));
            a.setOrt(rs.getString("ort"));
            a.setLand(rs.getString("land"));
            return a;
        });
        return list.isEmpty() ? null : list.get(0);
    }

    public Adresse createAdresse(Adresse adresse) {
        String sql = "INSERT INTO adresse (aktiv, strasse, hausnummer, plz, ort, land) VALUES (?, ?, ?, ?, ?, ?) RETURNING adresse_id";
        Integer id = jdbc.queryForObject(sql, new Object[]{
                adresse.isAktiv(),
                adresse.getStrasse(),
                adresse.getHausnummer(),
                adresse.getPlz(),
                adresse.getOrt(),
                adresse.getLand()
        }, Integer.class);
        adresse.setAdresseId(id);
        return adresse;
    }

    public Adresse updateAdresse(int id, Adresse adresse) {
        String sql = "UPDATE adresse SET aktiv = ?, strasse = ?, hausnummer = ?, plz = ?, ort = ?, land = ? WHERE adresse_id = ?";
        jdbc.update(sql,
                adresse.isAktiv(),
                adresse.getStrasse(),
                adresse.getHausnummer(),
                adresse.getPlz(),
                adresse.getOrt(),
                adresse.getLand(),
                id
        );
        return findById(id);
    }
}
