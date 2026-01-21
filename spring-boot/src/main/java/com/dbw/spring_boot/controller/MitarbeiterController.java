package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.model.Mitarbeiter;
import com.dbw.spring_boot.repository.MitarbeiterRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/mitarbeiter")
public class MitarbeiterController {

    private final MitarbeiterRepository repository;

    public MitarbeiterController(MitarbeiterRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Mitarbeiter> alleMitarbeiter() {
        return repository.findAll();
    }

    @GetMapping(params = "id")
    public ResponseEntity<Mitarbeiter> einzelnerMitarbeiter(@RequestParam int id) {
        Mitarbeiter m = repository.findById(id);
        if (m == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(m);
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
