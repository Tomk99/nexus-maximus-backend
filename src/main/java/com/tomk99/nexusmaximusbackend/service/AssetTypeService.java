package com.tomk99.nexusmaximusbackend.service;

import com.tomk99.nexusmaximusbackend.dto.AssetTypeDto;
import com.tomk99.nexusmaximusbackend.model.investment.AssetType;
import com.tomk99.nexusmaximusbackend.model.investment.Worksheet;
import com.tomk99.nexusmaximusbackend.repositories.AssetTypeRepository;
import com.tomk99.nexusmaximusbackend.repositories.WorksheetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssetTypeService {

    private final AssetTypeRepository assetTypeRepository;
    private final WorksheetRepository worksheetRepository;

    public AssetTypeService(AssetTypeRepository assetTypeRepository, WorksheetRepository worksheetRepository) {
        this.assetTypeRepository = assetTypeRepository;
        this.worksheetRepository = worksheetRepository;
    }

    @Transactional(readOnly = true)
    public List<AssetTypeDto> getAssetTypesForWorksheet(Long worksheetId) {
        return assetTypeRepository.findByWorksheetId(worksheetId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public AssetTypeDto createAssetType(Long worksheetId, AssetTypeDto assetTypeDto) {
        Worksheet worksheet = worksheetRepository.findById(worksheetId)
                .orElseThrow(() -> new RuntimeException("Worksheet not found with id: " + worksheetId));

        AssetType assetType = new AssetType();
        assetType.setName(assetTypeDto.name());
        assetType.setColor(assetTypeDto.color());
        assetType.setWorksheet(worksheet);

        AssetType saved = assetTypeRepository.save(assetType);
        return toDto(saved);
    }

    @Transactional
    public AssetTypeDto updateAssetType(Long assetTypeId, AssetTypeDto assetTypeDto) {
        AssetType assetType = assetTypeRepository.findById(assetTypeId)
                .orElseThrow(() -> new RuntimeException("AssetType not found with id: " + assetTypeId));

        assetType.setName(assetTypeDto.name());
        assetType.setColor(assetTypeDto.color());

        AssetType updated = assetTypeRepository.save(assetType);
        return toDto(updated);
    }

    private AssetTypeDto toDto(AssetType assetType) {
        return new AssetTypeDto(assetType.getId(), assetType.getName(), assetType.getColor());
    }
}