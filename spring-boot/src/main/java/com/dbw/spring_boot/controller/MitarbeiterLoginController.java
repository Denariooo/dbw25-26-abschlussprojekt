package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.dto.LoginRequestDTO;
import com.dbw.spring_boot.dto.LoginResponseDTO;
import com.dbw.spring_boot.model.Mitarbeiter;
import com.dbw.spring_boot.repository.MitarbeiterRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class MitarbeiterLoginController {

    private final MitarbeiterRepository repository;

    public MitarbeiterLoginController(MitarbeiterRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/mitarbeiter")
    public ResponseEntity<LoginResponseDTO> loginMitarbeiter(@RequestBody LoginRequestDTO request) {
        Mitarbeiter m = repository.findByPersonalNrAndPasswort(
                request.getPersonalNr(),
                request.getPasswort()
        );

        if (m == null) {
            LoginResponseDTO empty = new LoginResponseDTO();
            empty.setPersonalNr(null);
            empty.setPasswort(null);
            empty.setEmail(null);
            empty.setVorname(null);
            empty.setNachname(null);
            return ResponseEntity.status(401).body(empty);
        }

        LoginResponseDTO response = new LoginResponseDTO();
        response.setPersonalNr(m.getPersonalNr());
        response.setPasswort(null);
        response.setEmail(m.getEmail());
        response.setVorname(m.getVorname());
        response.setNachname(m.getNachname());
        return ResponseEntity.ok(response);
    }
}
