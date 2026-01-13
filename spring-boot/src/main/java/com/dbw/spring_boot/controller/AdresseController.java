package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.model.Adresse;
import com.dbw.spring_boot.repository.AdresseRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adressen")
public class AdresseController {

    private final AdresseRepository adresseRepository;

    public AdresseController(AdresseRepository adresseRepository) {
        this.adresseRepository = adresseRepository;
    }

    @GetMapping
    public List<Adresse> getAdressen(@RequestParam(required = false) Integer id) {
        if (id != null) {
            Adresse a = adresseRepository.findById(id);
            return a != null ? List.of(a) : List.of();
        }
        return adresseRepository.findAll();
    }

    @PostMapping
    public Adresse createAdresse(@RequestBody Adresse adresse) {
        return adresseRepository.createAdresse(adresse);
    }

    @PutMapping("/{id}")
    public Adresse updateAdresse(@PathVariable int id, @RequestBody Adresse adresse) {
        return adresseRepository.updateAdresse(id, adresse);
    }
}
