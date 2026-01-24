package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.model.Kunde;
import com.dbw.spring_boot.model.KundeAdresse;
import com.dbw.spring_boot.repository.AdresseRepository;
import com.dbw.spring_boot.repository.KundeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/kunden")
public class KundeController {
    private final KundeRepository kundeRepository;
    private final AdresseRepository adresseRepository;

    public KundeController(KundeRepository kundeRepository, AdresseRepository adresseRepository) {
        this.kundeRepository = kundeRepository;
        this.adresseRepository = adresseRepository;
    }

    @GetMapping
    public ResponseEntity<List<Kunde>> getKunden(@RequestParam(required = false) Integer id, @RequestParam(required = false) String email) {
        List<Kunde> kunden;
        if (id != null) {
            Kunde k = kundeRepository.findById(id);
            if (k == null) return ResponseEntity.notFound().build();
            List<KundeAdresse> kundeAdressen = adresseRepository.findByKundeId(k.getKundeId());
            k.setAdressen(kundeAdressen);
            kunden = List.of(k);
        } else if (email != null) {
            Kunde k = kundeRepository.findByEmail(email);
            if (k == null) return ResponseEntity.notFound().build();
            List<KundeAdresse> kundeAdressen = adresseRepository.findByKundeId(k.getKundeId());
            k.setAdressen(kundeAdressen);
            kunden = List.of(k);
        } else {
            kunden = kundeRepository.findAll();
            for (Kunde k : kunden) {
                List<KundeAdresse> kundeAdressen = adresseRepository.findByKundeId(k.getKundeId());
                k.setAdressen(kundeAdressen);
            }
        }
        return ResponseEntity.ok(kunden);
    }

    @PostMapping
    public ResponseEntity<Kunde> createKunde(@RequestBody Kunde kunde) {
        try {
            Kunde neu = kundeRepository.createKunde(kunde);
            URI location = URI.create("/kunden?id=" + neu.getKundeId());
            return ResponseEntity.created(location).body(neu);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Kunde> updateKunde(@PathVariable int id, @RequestBody Kunde kunde) {
        Kunde existierender = kundeRepository.findById(id);
        if (existierender == null) return ResponseEntity.notFound().build();
        Kunde aktualisiert = kundeRepository.updateKunde(id, kunde);
        return ResponseEntity.ok(aktualisiert);
    }
}
