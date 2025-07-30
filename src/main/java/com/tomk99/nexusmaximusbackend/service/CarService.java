package com.tomk99.nexusmaximusbackend.service;

import com.tomk99.nexusmaximusbackend.dto.CreateRefuelingRequestDto;
import com.tomk99.nexusmaximusbackend.dto.MaintenanceDto;
import com.tomk99.nexusmaximusbackend.dto.RefuelingDto;
import com.tomk99.nexusmaximusbackend.model.Maintenance;
import com.tomk99.nexusmaximusbackend.model.Refueling;
import com.tomk99.nexusmaximusbackend.repositories.MaintenanceRepository;
import com.tomk99.nexusmaximusbackend.repositories.RefuelingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarService {

    private final RefuelingRepository refuelingRepository;
    private final MaintenanceRepository maintenanceRepository;

    public CarService(RefuelingRepository refuelingRepository, MaintenanceRepository maintenanceRepository) {
        this.refuelingRepository = refuelingRepository;
        this.maintenanceRepository = maintenanceRepository;
    }

    @Transactional(readOnly = true)
    public List<RefuelingDto> getAllRefuelings() {
        return refuelingRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public RefuelingDto createRefueling(CreateRefuelingRequestDto requestDto) {
        Refueling refueling = new Refueling();
        refueling.setDate(requestDto.date());
        refueling.setOdometer(requestDto.odometer());
        refueling.setLiters(requestDto.liters());
        Refueling saved = refuelingRepository.save(refueling);
        return toDto(saved);
    }

    @Transactional
    public RefuelingDto updateRefueling(Long id, RefuelingDto refuelingDto) {
        Refueling refueling = refuelingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Refueling not found with id: " + id));
        refueling.setDate(refuelingDto.date());
        refueling.setOdometer(refuelingDto.odometer());
        refueling.setLiters(refuelingDto.liters());
        Refueling updated = refuelingRepository.save(refueling);
        return toDto(updated);
    }

    @Transactional
    public void deleteRefueling(Long id) {
        refuelingRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<MaintenanceDto> getAllMaintenances() {
        return maintenanceRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public MaintenanceDto createMaintenance(MaintenanceDto maintenanceDto) {
        Maintenance maintenance = new Maintenance();
        maintenance.setDate(maintenanceDto.date());
        maintenance.setOdometer(maintenanceDto.odometer());
        maintenance.setDescription(maintenanceDto.description());
        Maintenance saved = maintenanceRepository.save(maintenance);
        return toDto(saved);
    }

    @Transactional
    public MaintenanceDto updateMaintenance(Long id, MaintenanceDto maintenanceDto) {
        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maintenance not found with id: " + id));
        maintenance.setDate(maintenanceDto.date());
        maintenance.setOdometer(maintenanceDto.odometer());
        maintenance.setDescription(maintenanceDto.description());
        Maintenance updated = maintenanceRepository.save(maintenance);
        return toDto(updated);
    }

    @Transactional
    public void deleteMaintenance(Long id) {
        maintenanceRepository.deleteById(id);
    }

    private RefuelingDto toDto(Refueling refueling) {
        return new RefuelingDto(
                refueling.getId(),
                refueling.getDate(),
                refueling.getOdometer(),
                refueling.getLiters()
        );
    }

    private MaintenanceDto toDto(Maintenance maintenance) {
        return new MaintenanceDto(
                maintenance.getId(),
                maintenance.getDate(),
                maintenance.getOdometer(),
                maintenance.getDescription()
        );
    }
}