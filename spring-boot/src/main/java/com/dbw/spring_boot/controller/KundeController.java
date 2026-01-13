package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.model.Kunde;
import com.dbw.spring_boot.repository.KundeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kunden")
public class KundeController {

    private final KundeRepository kundeRepository;

    public KundeController(KundeRepository kundeRepository) {
        this.kundeRepository = kundeRepository;
    }

    @GetMapping
    public List<Kunde> getKunden(@RequestParam(required = false) Integer id,
                                 @RequestParam(required = false) String email) {
        if (id != null) {
            Kunde k = kundeRepository.findById(id);
            return k != null ? List.of(k) : List.of();
        }
        if (email != null) {
            Kunde k = kundeRepository.findByEmail(email);
            return k != null ? List.of(k) : List.of();
        }
        return kundeRepository.findAll();
    }

    @PostMapping
    public Kunde createKunde(@RequestBody Kunde kunde) {
        // Adressen ignorieren, nur Email/Vorname/Nachname/Passwort
        return kundeRepository.createKunde(kunde);
    }

    @PutMapping("/{id}")
    public Kunde updateKunde(@PathVariable int id, @RequestBody Kunde kunde) {
        // Adressen bleiben unverändert
        return kundeRepository.updateKunde(id, kunde);
    }
}
