package com.tomk99.nexusmaximusbackend.service;

import com.tomk99.nexusmaximusbackend.dto.WorksheetDto;
import com.tomk99.nexusmaximusbackend.model.investment.Worksheet;
import com.tomk99.nexusmaximusbackend.repositories.WorksheetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorksheetService {

    private final WorksheetRepository worksheetRepository;

    public WorksheetService(WorksheetRepository worksheetRepository) {
        this.worksheetRepository = worksheetRepository;
    }

    @Transactional(readOnly = true)
    public List<WorksheetDto> getAllWorksheets() {
        return worksheetRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public WorksheetDto createWorksheet(WorksheetDto worksheetDto) {
        Worksheet worksheet = new Worksheet();
        worksheet.setName(worksheetDto.name());
        Worksheet saved = worksheetRepository.save(worksheet);
        return toDto(saved);
    }

    @Transactional
    public WorksheetDto updateWorksheet(Long id, WorksheetDto worksheetDto) {
        Worksheet worksheet = worksheetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Worksheet not found with id: " + id));
        worksheet.setName(worksheetDto.name());
        Worksheet updated = worksheetRepository.save(worksheet);
        return toDto(updated);
    }

    @Transactional
    public void deleteWorksheet(Long id) {
        worksheetRepository.deleteById(id);
    }

    private WorksheetDto toDto(Worksheet worksheet) {
        return new WorksheetDto(worksheet.getId(), worksheet.getName());
    }
}