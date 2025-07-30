package com.tomk99.nexusmaximusbackend.service;

import com.tomk99.nexusmaximusbackend.dto.AssetValueDto;
import com.tomk99.nexusmaximusbackend.dto.InvestmentSnapshotDto;
import com.tomk99.nexusmaximusbackend.model.investment.*;
import com.tomk99.nexusmaximusbackend.repositories.AssetTypeRepository;
import com.tomk99.nexusmaximusbackend.repositories.InvestmentSnapshotRepository;
import com.tomk99.nexusmaximusbackend.repositories.WorksheetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InvestmentService {

    private final InvestmentSnapshotRepository investmentSnapshotRepository;
    private final AssetTypeRepository assetTypeRepository;
    private final WorksheetRepository worksheetRepository;

    public InvestmentService(InvestmentSnapshotRepository investmentSnapshotRepository,
                             AssetTypeRepository assetTypeRepository,
                             WorksheetRepository worksheetRepository) {
        this.investmentSnapshotRepository = investmentSnapshotRepository;
        this.assetTypeRepository = assetTypeRepository;
        this.worksheetRepository = worksheetRepository;
    }

    @Transactional(readOnly = true)
    public List<InvestmentSnapshotDto> getSnapshotsForWorksheet(Long worksheetId) {
        List<InvestmentSnapshot> snapshots = investmentSnapshotRepository.findByWorksheetId(worksheetId);
        Map<Long, AssetType> assetTypeMap = assetTypeRepository.findByWorksheetId(worksheetId)
                .stream()
                .collect(Collectors.toMap(AssetType::getId, assetType -> assetType));

        return snapshots.stream()
                .map(snapshot -> toDto(snapshot, assetTypeMap))
                .collect(Collectors.toList());
    }

    @Transactional
    public InvestmentSnapshotDto createSnapshot(Long worksheetId, InvestmentSnapshotDto snapshotDto) {
        Worksheet worksheet = worksheetRepository.findById(worksheetId)
                .orElseThrow(() -> new RuntimeException("Worksheet not found with id: " + worksheetId));

        InvestmentSnapshot snapshot = fromDto(snapshotDto);
        snapshot.setWorksheet(worksheet);

        InvestmentSnapshot saved = investmentSnapshotRepository.save(snapshot);
        return toDto(saved);
    }

    @Transactional
    public InvestmentSnapshotDto updateSnapshot(Long id, InvestmentSnapshotDto snapshotDto) {
        InvestmentSnapshot snapshot = investmentSnapshotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("InvestmentSnapshot not found with id: " + id));

        snapshot.setDate(snapshotDto.date());
        snapshot.setAssets(fromDto(snapshotDto).getAssets());

        InvestmentSnapshot updated = investmentSnapshotRepository.save(snapshot);
        return toDto(updated);
    }

    @Transactional
    public void deleteSnapshot(Long id) {
        investmentSnapshotRepository.deleteById(id);
    }

    @Transactional
    public void deleteAssetTypeAndCleanSnapshots(Long assetTypeId) {
        investmentSnapshotRepository.deleteAssetValuesByAssetTypeId(assetTypeId);
        assetTypeRepository.deleteById(assetTypeId);
    }

    public void exportInvestmentDataToCsv(PrintWriter writer) {
        List<Worksheet> worksheets = worksheetRepository.findAll();

        try {
            writer.println("TYPE,ID,NAME");
            for (Worksheet worksheet : worksheets) {
                writer.println(String.join(",",
                        "WORKSHEET",
                        worksheet.getId().toString(),
                        "\"" + worksheet.getName().replace("\"", "\"\"") + "\""
                ));
            }
            writer.println();

            writer.println("TYPE,ID,NAME,COLOR,WORKSHEET_ID");
            for (Worksheet worksheet : worksheets) {
                List<AssetType> assetTypes = assetTypeRepository.findByWorksheetId(worksheet.getId());
                for (AssetType assetType : assetTypes) {
                    writer.println(String.join(",",
                            "ASSET_TYPE",
                            assetType.getId().toString(),
                            "\"" + assetType.getName().replace("\"", "\"\"") + "\"",
                            assetType.getColor(),
                            assetType.getWorksheet().getId().toString()
                    ));
                }
            }
            writer.println();

            writer.println("TYPE,SNAPSHOT_ID,DATE,ASSET_TYPE_ID,VALUE");
            for (Worksheet worksheet : worksheets) {
                List<InvestmentSnapshot> snapshots = investmentSnapshotRepository.findByWorksheetId(worksheet.getId());
                for (InvestmentSnapshot snapshot : snapshots) {
                    for (AssetValue assetValue : snapshot.getAssets()) {
                        writer.println(String.join(",",
                                "SNAPSHOT_DATA",
                                snapshot.getId().toString(),
                                snapshot.getDate().toString(),
                                assetValue.getAssetTypeId().toString(),
                                String.valueOf(assetValue.getValue())
                        ));
                    }
                }
            }
        } finally {
            writer.flush();
            writer.close();
        }
    }

    private InvestmentSnapshotDto toDto(InvestmentSnapshot snapshot) {
        Map<Long, AssetType> assetTypeMap = assetTypeRepository.findByWorksheetId(snapshot.getWorksheet().getId())
                .stream()
                .collect(Collectors.toMap(AssetType::getId, assetType -> assetType));
        return toDto(snapshot, assetTypeMap);
    }

    private InvestmentSnapshotDto toDto(InvestmentSnapshot snapshot, Map<Long, AssetType> assetTypeMap) {
        List<AssetValueDto> assetDtos = snapshot.getAssets().stream()
                .map(assetValue -> {
                    AssetType type = assetTypeMap.get(assetValue.getAssetTypeId());
                    String name = (type != null) ? type.getName() : "Ismeretlen";
                    String color = (type != null) ? type.getColor() : "#808080";
                    return new AssetValueDto(
                            assetValue.getAssetTypeId(),
                            name,
                            color,
                            assetValue.getValue()
                    );
                })
                .collect(Collectors.toList());

        return new InvestmentSnapshotDto(snapshot.getId(), snapshot.getDate(), assetDtos);
    }

    private InvestmentSnapshot fromDto(InvestmentSnapshotDto dto) {
        InvestmentSnapshot snapshot = new InvestmentSnapshot();
        snapshot.setDate(dto.date());
        List<AssetValue> assets = dto.assets().stream()
                .map(assetDto -> new AssetValue(
                        assetDto.assetTypeId(),
                        assetDto.name(),
                        assetDto.color(),
                        assetDto.value()))
                .collect(Collectors.toList());
        snapshot.setAssets(assets);
        return snapshot;
    }
}