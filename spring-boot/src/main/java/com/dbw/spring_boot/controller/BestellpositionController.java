package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.model.Bestellposition;
import com.dbw.spring_boot.repository.BestellpositionRepository;
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
    public List<Bestellposition> getBestellpositionen(@RequestParam(required = false) Integer id) {
        if (id != null) {
            Bestellposition p = repo.findById(id);
            return p != null ? List.of(p) : List.of();
        }
        return repo.findAll();
    }

    @PostMapping
    public Bestellposition createBestellposition(@RequestBody Bestellposition position) {
        return repo.createBestellposition(position);
    }

    @DeleteMapping("/{id}")
    public void deleteBestellposition(@PathVariable int id) {
        repo.deleteById(id);
    }
}
