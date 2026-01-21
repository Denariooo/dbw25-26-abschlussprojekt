package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.model.Bestellposition;
import com.dbw.spring_boot.repository.BestellpositionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bestellpositionen")
public class BestellpositionController {

    private final BestellpositionRepository repo;

    public BestellpositionController(BestellpositionRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public ResponseEntity<List<Bestellposition>> getBestellpositionen(
            @RequestParam(required = false) Integer id) {

        if (id != null) {
            Bestellposition p = repo.findById(id);
            if (p == null) {
                return ResponseEntity.notFound().build(); // 404
            }
            return ResponseEntity.ok(List.of(p));
        }

        return ResponseEntity.ok(repo.findAll());
    }

    // ✅ 201 Created
    @PostMapping
    public ResponseEntity<Bestellposition> createBestellposition(
            @RequestBody Bestellposition position) {

        Bestellposition created = repo.createBestellposition(position);
        return ResponseEntity.status(201).body(created);
    }

    // ✅ 204 oder 404
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBestellposition(@PathVariable int id) {

        boolean deleted = repo.deleteById(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build(); // 204
    }
}
