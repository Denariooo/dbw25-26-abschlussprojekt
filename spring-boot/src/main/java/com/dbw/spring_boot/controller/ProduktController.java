package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.model.Produkt;
import com.dbw.spring_boot.repository.ProduktRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/produkte")
public class ProduktController {

    private final ProduktRepository produktRepository;

    public ProduktController(ProduktRepository produktRepository) {
        this.produktRepository = produktRepository;
    }

    @GetMapping
    public ResponseEntity<List<Produkt>> getProdukte(@RequestParam(required = false) String sku) {
        if (sku != null) {
            Produkt p = produktRepository.findBySku(sku);
            if (p == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(List.of(p));
        }
        return ResponseEntity.ok(produktRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Produkt> createProdukt(@RequestBody Produkt produkt) {
        Produkt created = produktRepository.createProdukt(produkt);
        URI location = URI.create("/produkte/" + created.getSku());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{sku}/lagerbestand")
    public ResponseEntity<Produkt> updateLagerbestand(@PathVariable String sku, @RequestParam int lagerbestand) {
        Produkt updated = produktRepository.updateLagerbestand(sku, lagerbestand);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{sku}")
    public ResponseEntity<Void> deleteProdukt(@PathVariable String sku) {
        boolean deleted = produktRepository.deleteBySku(sku);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }
}
