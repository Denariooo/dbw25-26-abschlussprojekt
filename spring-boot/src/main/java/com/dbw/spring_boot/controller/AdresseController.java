package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.model.Adresse;
import com.dbw.spring_boot.repository.AdresseRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/adressen")
public class AdresseController {

    private final AdresseRepository adresseRepository;

    public AdresseController(AdresseRepository adresseRepository) {
        this.adresseRepository = adresseRepository;
    }

    @GetMapping
    public ResponseEntity<List<Adresse>> getAdressen(@RequestParam(required = false) Integer id) {
        if (id != null) {
            Adresse a = adresseRepository.findById(id);
            if (a == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(List.of(a));
        }
        return ResponseEntity.ok(adresseRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Adresse> getAdresseById(@PathVariable int id) {
        Adresse a = adresseRepository.findById(id);
        if (a == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(a); // 200
    }

    // POST /adressen
    @PostMapping
    public ResponseEntity<Adresse> createAdresse(@RequestBody Adresse adresse) {
        Adresse created = adresseRepository.createAdresse(adresse);
        // Location-Header setzen
        URI location = URI.create("/adressen/" + created.getAdresseId());
        return ResponseEntity.created(location).body(created); // 201
    }

    // PUT /adressen/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Adresse> updateAdresse(@PathVariable int id, @RequestBody Adresse adresse) {
        Adresse existing = adresseRepository.findById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build(); // 404
        }
        Adresse updated = adresseRepository.updateAdresse(id, adresse);
        return ResponseEntity.ok(updated); // 200
    }
}
