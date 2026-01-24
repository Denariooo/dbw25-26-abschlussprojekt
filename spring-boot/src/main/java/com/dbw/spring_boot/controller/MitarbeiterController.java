package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.model.Mitarbeiter;
import com.dbw.spring_boot.repository.MitarbeiterRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mitarbeiter")
public class MitarbeiterController {

    private final MitarbeiterRepository repository;

    public MitarbeiterController(MitarbeiterRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<Mitarbeiter>> getMitarbeiter(@RequestParam(required = false) Integer id) {
        if (id != null) {
            Mitarbeiter m = repository.findById(id);
            if (m == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(List.of(m));
        }
        List<Mitarbeiter> alle = repository.findAll();
        return ResponseEntity.ok(alle);
    }

    @PostMapping
    public ResponseEntity<Mitarbeiter> neuerMitarbeiter(@RequestBody Mitarbeiter m) {
        Mitarbeiter saved = repository.save(m);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> loescheMitarbeiter(@PathVariable int id) {
        int rowsAffected = repository.deleteById(id);
        if (rowsAffected == 0) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }
}
