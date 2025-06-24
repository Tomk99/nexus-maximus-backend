package com.tomk99.nexusmaximusbackend.controller;

import com.tomk99.nexusmaximusbackend.model.Maintenance;
import com.tomk99.nexusmaximusbackend.model.Refueling;
import com.tomk99.nexusmaximusbackend.repositories.MaintenanceRepository;
import com.tomk99.nexusmaximusbackend.repositories.RefuelingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class CarController {

    @Autowired
    private RefuelingRepository refuelingRepository;

    @Autowired
    private MaintenanceRepository maintenanceRepository;

    @GetMapping("/refuelings")
    public List<Refueling> getAllRefuelings() {
        return refuelingRepository.findAll();
    }

    @PostMapping("/refuelings")
    public List<Refueling> createRefueling(@RequestBody Refueling refueling) {
        refuelingRepository.save(refueling);
        return refuelingRepository.findAll();
    }

    @PutMapping("/refuelings/{id}")
    public List<Refueling> updateRefueling(@PathVariable Long id, @RequestBody Refueling refuelingDetails) {
        Refueling refueling = refuelingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Refueling not found with id: " + id));

        refueling.setDate(refuelingDetails.getDate());
        refueling.setOdometer(refuelingDetails.getOdometer());
        refueling.setLiters(refuelingDetails.getLiters());

        refuelingRepository.save(refueling);
        return refuelingRepository.findAll();
    }

    @DeleteMapping("/refuelings/{id}")
    public ResponseEntity<Void> deleteRefueling(@PathVariable Long id) {
        refuelingRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/maintenances")
    public List<Maintenance> getAllMaintenances() {
        return maintenanceRepository.findAll();
    }

    @PostMapping("/maintenances")
    public List<Maintenance> createMaintenance(@RequestBody Maintenance maintenance) {
        maintenanceRepository.save(maintenance);
        return maintenanceRepository.findAll();
    }

    @PutMapping("/maintenances/{id}")
    public List<Maintenance> updateMaintenance(@PathVariable Long id, @RequestBody Maintenance maintenanceDetails) {
        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maintenance not found with id: " + id));

        maintenance.setDate(maintenanceDetails.getDate());
        maintenance.setOdometer(maintenanceDetails.getOdometer());
        maintenance.setDescription(maintenanceDetails.getDescription());

        maintenanceRepository.save(maintenance);
        return maintenanceRepository.findAll();
    }

    @DeleteMapping("/maintenances/{id}")
    public ResponseEntity<Void> deleteMaintenance(@PathVariable Long id) {
        maintenanceRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}