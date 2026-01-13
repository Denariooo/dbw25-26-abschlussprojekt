package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.model.Bestellung;
import com.dbw.spring_boot.repository.BestellungRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bestellungen")
public class BestellungController {

    private final BestellungRepository repository;

    public BestellungController(BestellungRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Bestellung> getBestellungen(@RequestParam(required = false) Integer id) {
        if (id != null) {
            return List.of(repository.findById(id));
        }
        return repository.findAll();
    }

    @PostMapping
    public Bestellung createBestellung(@RequestBody Bestellung bestellung) {
        return repository.createBestellung(bestellung);
    }

    @DeleteMapping("/{id}")
    public void deleteBestellung(@PathVariable int id) {
        repository.deleteBestellung(id);
    }
}
