package com.dbw.spring_boot.repository;

import com.dbw.spring_boot.model.Adresse;
import com.dbw.spring_boot.model.KundeAdresse;
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

    public List<KundeAdresse> findByKundeId(int kundeId) {
        String sql = "SELECT a.adresse_id, a.aktiv, a.strasse, a.hausnummer, a.plz, a.ort, a.land, ka.typ " +
                "FROM adresse a " +
                "JOIN kunde_hat_adressen ka ON a.adresse_id = ka.adresse_id " +
                "WHERE ka.kunde_id = ?";

        return jdbc.query(sql, new Object[]{kundeId}, (rs, rowNum) -> {
            Adresse adresse = new Adresse();
            adresse.setAdresseId(rs.getInt("adresse_id"));
            adresse.setAktiv(rs.getBoolean("aktiv"));
            adresse.setStrasse(rs.getString("strasse"));
            adresse.setHausnummer(rs.getString("hausnummer"));
            adresse.setPlz(rs.getInt("plz"));
            adresse.setOrt(rs.getString("ort"));
            adresse.setLand(rs.getString("land"));

            KundeAdresse ka = new KundeAdresse();
            ka.setAdresse(adresse);
            ka.setTyp(rs.getString("typ"));

            return ka;
        });
    }

}
