package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.model.Produkt;
import com.dbw.spring_boot.repository.ProduktRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produkte")
public class ProduktController {

    private final ProduktRepository produktRepository;

    public ProduktController(ProduktRepository produktRepository) {
        this.produktRepository = produktRepository;
    }

    @GetMapping
    public List<Produkt> getProdukte(@RequestParam(required = false) String sku) {
        if (sku != null) {
            Produkt p = produktRepository.findBySku(sku);
            return p != null ? List.of(p) : List.of();
        }
        return produktRepository.findAll();
    }

    // Neues Produkt erstellen
    @PostMapping
    public Produkt createProdukt(@RequestBody Produkt produkt) {
        return produktRepository.createProdukt(produkt);
    }

    // Lagerbestand eines Produkts aktualisieren
    @PutMapping("/{sku}/lagerbestand")
    public Produkt updateLagerbestand(@PathVariable String sku, @RequestParam int lagerbestand) {
        return produktRepository.updateLagerbestand(sku, lagerbestand);
    }

    // Produkt löschen
    @DeleteMapping("/{sku}")
    public void deleteProdukt(@PathVariable String sku) {
        produktRepository.deleteBySku(sku);
    }
}
