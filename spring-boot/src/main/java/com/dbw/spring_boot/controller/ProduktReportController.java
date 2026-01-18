package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.repository.ReportRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/report/produkt")
public class ProduktReportController {

    private final ReportRepository reportRepo;

    public ProduktReportController(ReportRepository reportRepo) {
        this.reportRepo = reportRepo;
    }

    @GetMapping("/verkaufszahlen")
    public List<Map<String, Object>> getProduktVerkaufszahlen() {
        List<Map<String, Object>> rows = reportRepo.getProduktVerkaufszahlen();

        return rows.stream().map(row -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("sku", row.get("sku"));
            map.put("name", row.get("name"));
            map.put("gesamtVerkaufteMenge", row.get("gesamtverkaufteMenge"));
            map.put("umsatz", row.get("umsatz"));
            map.put("anzahlBestellungen", row.get("anzahlBestellungen"));
            return map;
        }).toList();
    }

}
