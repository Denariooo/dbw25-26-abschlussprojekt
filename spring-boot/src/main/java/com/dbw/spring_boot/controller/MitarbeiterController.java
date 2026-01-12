package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.model.Mitarbeiter;
import com.dbw.spring_boot.repository.MitarbeiterRepository;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/mitarbeiter")
public class MitarbeiterController {

    private final MitarbeiterRepository repository;

    public MitarbeiterController(MitarbeiterRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Mitarbeiter> alleMitarbeiter() throws SQLException {
        return repository.findAll();
    }

    @GetMapping(params = "id")
    public Mitarbeiter einzelnerMitarbeiter(@RequestParam int id) throws SQLException {
        return repository.findById(id);
    }

    @PostMapping
    public void neuerMitarbeiter(@RequestBody Mitarbeiter m) throws SQLException {
        repository.save(m);
    }

    @DeleteMapping
    public void loescheMitarbeiter(@RequestParam int id) throws SQLException {
        repository.deleteById(id);
    }
}
