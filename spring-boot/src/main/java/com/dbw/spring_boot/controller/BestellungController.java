package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.dto.BestellpositionDTO;
import com.dbw.spring_boot.dto.BestellungDTO;
import com.dbw.spring_boot.dto.ProduktDTO;
import com.dbw.spring_boot.model.Bestellung;
import com.dbw.spring_boot.model.Produkt;
import com.dbw.spring_boot.repository.BestellungRepository;
import com.dbw.spring_boot.repository.ProduktRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bestellungen")
public class BestellungController {

    private final BestellungRepository bestellungRepo;
    private final ProduktRepository produktRepo;

    public BestellungController(BestellungRepository repository, ProduktRepository produktRepository) {
        this.produktRepo = produktRepository;
        this.bestellungRepo = repository;
    }

    @GetMapping
    public ResponseEntity<List<BestellungDTO>> getBestellungen(@RequestParam(required = false) Integer id) {
        List<Bestellung> bestellungen = (id != null) ?
                List.of(bestellungRepo.findById(id)) :
                bestellungRepo.findAll();

        if (bestellungen.isEmpty() || bestellungen.get(0) == null) {
            return ResponseEntity.notFound().build();
        }

        List<BestellungDTO> dtos = bestellungen.stream().map(b -> {
            BestellungDTO dto = new BestellungDTO();
            dto.setBestellungId(b.getBestellungId());
            dto.setKundeId(b.getKundeId());
            dto.setPersonalnr(b.getPersonalnr());
            dto.setDatum(b.getDatum());
            dto.setStatus(b.getStatus());

            List<BestellpositionDTO> positionDtos = b.getPositionen().stream().map(bp -> {
                BestellpositionDTO bpDto = new BestellpositionDTO();
                bpDto.setPositionId(bp.getPositionId());
                bpDto.setBestellungId(bp.getBestellungId());
                bpDto.setMenge(bp.getMenge());
                bpDto.setGesamtpreis(bp.getGesamtpreis());
                Produkt p = produktRepo.findBySku(bp.getProduktSku());
                if (p != null) {
                    ProduktDTO pDto = new ProduktDTO();
                    pDto.setSku(p.getSku());
                    pDto.setName(p.getName());
                    pDto.setPreis(p.getPreis());
                    pDto.setLagerbestand(p.getLagerbestand());
                    pDto.setAngelegtVon(p.getAngelegtVon());
                    bpDto.setProdukt(pDto);
                }

                return bpDto;
            }).collect(Collectors.toList());

            dto.setPositionen(positionDtos);
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<BestellungDTO> createBestellung(@RequestBody Bestellung bestellung) {
        try {
            Bestellung b = bestellungRepo.createBestellung(bestellung);
            BestellungDTO dto = new BestellungDTO();
            dto.setBestellungId(b.getBestellungId());
            dto.setKundeId(b.getKundeId());
            dto.setPersonalnr(b.getPersonalnr());
            dto.setDatum(b.getDatum());
            dto.setStatus(b.getStatus());
            dto.setPositionen(List.of()); // Neu erstellte Bestellung hat noch keine Positionen
            return ResponseEntity.status(201).body(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBestellung(@PathVariable int id) {
        bestellungRepo.deleteBestellung(id);
        return ResponseEntity.noContent().build(); // 204
    }
}
