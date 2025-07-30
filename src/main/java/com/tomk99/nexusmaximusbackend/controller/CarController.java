package com.tomk99.nexusmaximusbackend.controller;

import com.tomk99.nexusmaximusbackend.dto.MaintenanceDto;
import com.tomk99.nexusmaximusbackend.dto.RefuelingDto;
import com.tomk99.nexusmaximusbackend.service.CarService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/car/export/csv")
    public void exportCarDataToCsv(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/csv; charset=UTF-8");
        response.getWriter().write('\uFEFF');
        response.setHeader("Content-Disposition", "attachment; filename=\"car_data.csv\"");
        carService.exportCarDataToCsv(response.getWriter());
    }

    @GetMapping("/refuelings")
    public List<RefuelingDto> getAllRefuelings() {
        return carService.getAllRefuelings();
    }

    @PostMapping("/refuelings")
    @ResponseStatus(HttpStatus.CREATED)
    public RefuelingDto createRefueling(@RequestBody RefuelingDto refuelingDto) {
        return carService.createRefueling(refuelingDto);
    }

    @PutMapping("/refuelings/{id}")
    public RefuelingDto updateRefueling(@PathVariable Long id, @RequestBody RefuelingDto refuelingDetails) {
        return carService.updateRefueling(id, refuelingDetails);
    }

    @DeleteMapping("/refuelings/{id}")
    public ResponseEntity<Void> deleteRefueling(@PathVariable Long id) {
        carService.deleteRefueling(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/maintenances")
    public List<MaintenanceDto> getAllMaintenances() {
        return carService.getAllMaintenances();
    }

    @PostMapping("/maintenances")
    @ResponseStatus(HttpStatus.CREATED)
    public MaintenanceDto createMaintenance(@RequestBody MaintenanceDto maintenanceDto) {
        return carService.createMaintenance(maintenanceDto);
    }

    @PutMapping("/maintenances/{id}")
    public MaintenanceDto updateMaintenance(@PathVariable Long id, @RequestBody MaintenanceDto maintenanceDetails) {
        return carService.updateMaintenance(id, maintenanceDetails);
    }

    @DeleteMapping("/maintenances/{id}")
    public ResponseEntity<Void> deleteMaintenance(@PathVariable Long id) {
        carService.deleteMaintenance(id);
        return ResponseEntity.noContent().build();
    }
}