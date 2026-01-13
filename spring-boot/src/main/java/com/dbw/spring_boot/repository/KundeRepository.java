package com.dbw.spring_boot.repository;

import com.dbw.spring_boot.model.Adresse;
import com.dbw.spring_boot.model.Kunde;
import com.dbw.spring_boot.model.KundeAdresse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        return queryKunden("k.kunde_id = ?", id).stream().findFirst().orElse(null);
    }

    public Kunde findByEmail(String email) {
        return queryKunden("k.email = ?", email).stream().findFirst().orElse(null);
    }

    public List<Kunde> findAll() {
        return queryKunden(null, null);
    }

    private List<Kunde> queryKunden(String where, Object param) {
        String sql = BASE_SQL + (where != null ? " WHERE " + where : "");
        Map<Integer, Kunde> kunden = new LinkedHashMap<>();

        jdbc.query(sql,
                param == null ? new Object[]{} : new Object[]{param},
                rs -> {
                    int kid = rs.getInt("kunde_id");
                    Kunde kunde = kunden.computeIfAbsent(kid, id -> {
                        Kunde k = new Kunde();
                        k.setKundeId(id);
                        try {
                            k.setEmail(rs.getString("kunde_email"));
                            k.setVorname(rs.getString("kunde_vorname"));
                            k.setNachname(rs.getString("kunde_nachname"));
                            k.setPasswort(rs.getString("kunde_passwort"));
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        k.setAdressen(new ArrayList<>());
                        return k;
                    });

                    int aid = rs.getInt("adresse_id");
                    if (!rs.wasNull()) {
                        Adresse a = new Adresse();
                        a.setAdresseId(aid);
                        a.setAktiv(rs.getBoolean("adresse_aktiv"));
                        a.setStrasse(rs.getString("adresse_strasse"));
                        a.setHausnummer(rs.getString("adresse_hausnummer"));
                        a.setPlz(rs.getInt("adresse_plz"));
                        a.setOrt(rs.getString("adresse_ort"));
                        a.setLand(rs.getString("adresse_land"));

                        KundeAdresse ka = new KundeAdresse();
                        ka.setAdresse(a);
                        ka.setTyp(rs.getString("adresse_typ"));

                        kunde.getAdressen().add(ka);
                    }
                });

        return new ArrayList<>(kunden.values());
    }

    private static final String BASE_SQL = "SELECT " +
            "k.kunde_id AS kunde_id, " +
            "k.email AS kunde_email, " +
            "k.vorname AS kunde_vorname, " +
            "k.nachname AS kunde_nachname, " +
            "k.passwort AS kunde_passwort, " +
            "a.adresse_id AS adresse_id, " +
            "a.aktiv AS adresse_aktiv, " +
            "a.strasse AS adresse_strasse, " +
            "a.hausnummer AS adresse_hausnummer, " +
            "a.plz AS adresse_plz, " +
            "a.ort AS adresse_ort, " +
            "a.land AS adresse_land, " +
            "ka.typ AS adresse_typ " +
            "FROM kunde k " +
            "LEFT JOIN kunde_hat_adressen ka ON k.kunde_id = ka.kunde_id " +
            "LEFT JOIN adresse a ON ka.adresse_id = a.adresse_id";
}
