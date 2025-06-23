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
@CrossOrigin(origins = "http://localhost:3000")
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
    public Refueling createRefueling(@RequestBody Refueling refueling) {
        return refuelingRepository.save(refueling);
    }

    @PutMapping("/refuelings/{id}")
    public ResponseEntity<Refueling> updateRefueling(@PathVariable Long id, @RequestBody Refueling refuelingDetails) {
        Refueling refueling = refuelingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Refueling not found with id: " + id));

        refueling.setDate(refuelingDetails.getDate());
        refueling.setOdometer(refuelingDetails.getOdometer());
        refueling.setLiters(refuelingDetails.getLiters());

        final Refueling updatedRefueling = refuelingRepository.save(refueling);
        return ResponseEntity.ok(updatedRefueling);
    }

    @DeleteMapping("/refuelings/{id}")
    public ResponseEntity<Void> deleteRefueling(@PathVariable Long id) {
        Refueling refueling = refuelingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Refueling not found with id: " + id));

        refuelingRepository.delete(refueling);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/maintenances")
    public List<Maintenance> getAllMaintenances() {
        return maintenanceRepository.findAll();
    }

    @PostMapping("/maintenances")
    public Maintenance createMaintenance(@RequestBody Maintenance maintenance) {
        return maintenanceRepository.save(maintenance);
    }

    @PutMapping("/maintenances/{id}")
    public ResponseEntity<Maintenance> updateMaintenance(@PathVariable Long id, @RequestBody Maintenance maintenanceDetails) {
        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maintenance not found with id: " + id));

        maintenance.setDate(maintenanceDetails.getDate());
        maintenance.setOdometer(maintenanceDetails.getOdometer());
        maintenance.setDescription(maintenanceDetails.getDescription());

        final Maintenance updatedMaintenance = maintenanceRepository.save(maintenance);
        return ResponseEntity.ok(updatedMaintenance);
    }

    @DeleteMapping("/maintenances/{id}")
    public ResponseEntity<Void> deleteMaintenance(@PathVariable Long id) {
        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maintenance not found with id: " + id));

        maintenanceRepository.delete(maintenance);
        return ResponseEntity.noContent().build();
    }
}