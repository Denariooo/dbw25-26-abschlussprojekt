package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.dto.MitarbeiterUebersichtDTO;
import com.dbw.spring_boot.repository.ReportRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/report/mitarbeiter")
public class MitarbeiterReportController {

    private final ReportRepository reportRepository;

    public MitarbeiterReportController(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @GetMapping("/uebersicht")
    public List<MitarbeiterUebersichtDTO> getMitarbeiterUebersicht() {
        List<Map<String, Object>> rows = reportRepository.getMitarbeiterUebersicht();

        return rows.stream().map(row -> new MitarbeiterUebersichtDTO(
                ((Number) row.get("personalNr")).intValue(),
                ((Number) row.get("anzahlVerwalteterBestellungen")).intValue(),
                ((Number) row.get("anzahlAngelegterProdukte")).intValue()
        )).toList();
    }
}
