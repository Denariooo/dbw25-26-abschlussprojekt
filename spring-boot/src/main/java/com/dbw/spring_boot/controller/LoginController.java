package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.dto.LoginRequestDTO;
import com.dbw.spring_boot.dto.LoginResponseDTO;
import com.dbw.spring_boot.model.Mitarbeiter;
import com.dbw.spring_boot.repository.MitarbeiterRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final MitarbeiterRepository repository;

    public LoginController(MitarbeiterRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/mitarbeiter")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {

        Mitarbeiter m = repository.findByPersonalNrAndPasswort(
                request.getPersonalNr(),
                request.getPasswort()
        );

        LoginResponseDTO response = new LoginResponseDTO();

        if (m == null) {
            response.setPersonalNr(null);
            response.setPasswort(null);
            response.setEmail(null);
            response.setVorname(null);
            response.setNachname(null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        response.setPersonalNr(m.getPersonalNr());
        response.setPasswort(null);
        response.setEmail(m.getEmail());
        response.setVorname(m.getVorname());
        response.setNachname(m.getNachname());

        return ResponseEntity.ok(response);
    }
}
