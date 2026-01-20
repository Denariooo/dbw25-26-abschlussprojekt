package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.dto.MitarbeiterBestellstatusDTO;
import com.dbw.spring_boot.repository.ReportRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/report/mitarbeiter")
public class BestellstatusController {
    private final ReportRepository reportRepository;

    public BestellstatusController(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @GetMapping("/bestellstatus-uebersicht")
    public List<MitarbeiterBestellstatusDTO> getBestellstatusUebersicht() {
        return reportRepository.findMitarbeiterBestellstatusUebersicht();
    }
}
