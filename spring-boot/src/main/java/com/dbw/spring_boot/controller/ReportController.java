package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.repository.ReportRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/report/kunde")
public class ReportController {

    private final ReportRepository reportRepository;

    public ReportController(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @GetMapping("/summe-anzahl-bestellungen")
    public ResponseEntity<List<Map<String, Object>>> getKundeSummeAnzahlBestellungen() {
        List<Map<String, Object>> report = reportRepository.getKundeSummeAnzahlBestellungen();
        return ResponseEntity.ok(report);
    }
}