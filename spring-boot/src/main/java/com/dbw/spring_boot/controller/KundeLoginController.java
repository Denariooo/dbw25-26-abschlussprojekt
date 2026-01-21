package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.dto.KundeLoginRequest;
import com.dbw.spring_boot.dto.KundeLoginResponse;
import com.dbw.spring_boot.model.Adresse;
import com.dbw.spring_boot.model.Kunde;
import com.dbw.spring_boot.model.KundeAdresse;
import com.dbw.spring_boot.repository.AdresseRepository;
import com.dbw.spring_boot.repository.KundeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/login")
public class KundeLoginController {

    private final KundeRepository kundeRepository;
    private final AdresseRepository adresseRepository;

    public KundeLoginController(KundeRepository kundeRepository, AdresseRepository adresseRepository) {
        this.kundeRepository = kundeRepository;
        this.adresseRepository = adresseRepository;
    }

    @PostMapping("/kunde")
    public ResponseEntity<KundeLoginResponse> login(@RequestBody KundeLoginRequest request) {
        Kunde k = kundeRepository.findByEmailAndPasswort(request.getEmail(), request.getPasswort());

        if (k == null) {
            KundeLoginResponse empty = new KundeLoginResponse();
            empty.setAdressen(List.of());
            return ResponseEntity.status(401).body(empty);
        }

        /*List<KundeAdresse> kundeAdressen = adresseRepository.findByKundeId(k.getKundeId());
        List<Adresse> adressen = kundeAdressen.stream()
                .map(KundeAdresse::getAdresse)
                .toList();*/

        KundeLoginResponse response = new KundeLoginResponse();
        response.setKundeId(k.getKundeId());
        response.setEmail(k.getEmail());
        response.setVorname(k.getVorname());
        response.setNachname(k.getNachname());
        response.setPasswort(null);
        response.setAdressen(List.of());

        return ResponseEntity.ok(response);
    }
}
