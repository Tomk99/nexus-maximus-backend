package com.tomk99.nexusmaximusbackend.controller;

import com.tomk99.nexusmaximusbackend.model.investment.AssetType;
import com.tomk99.nexusmaximusbackend.model.investment.AssetValue;
import com.tomk99.nexusmaximusbackend.model.investment.InvestmentSnapshot;
import com.tomk99.nexusmaximusbackend.model.investment.Worksheet;
import com.tomk99.nexusmaximusbackend.repositories.AssetTypeRepository;
import com.tomk99.nexusmaximusbackend.repositories.InvestmentSnapshotRepository;
import com.tomk99.nexusmaximusbackend.repositories.WorksheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class InvestmentController {

    @Autowired
    private WorksheetRepository worksheetRepository;

    @Autowired
    private AssetTypeRepository assetTypeRepository;

    @Autowired
    private InvestmentSnapshotRepository investmentSnapshotRepository;


    @GetMapping("/worksheets")
    public List<Worksheet> getAllWorksheets() {
        return worksheetRepository.findAll();
    }

    @PostMapping("/worksheets")
    public Worksheet createWorksheet(@RequestBody Worksheet worksheet) {
        return worksheetRepository.save(worksheet);
    }

    @PutMapping("/worksheets/{id}")
    public ResponseEntity<Worksheet> updateWorksheet(@PathVariable Long id, @RequestBody Worksheet worksheetDetails) {
        Worksheet worksheet = worksheetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Worksheet not found with id: " + id));
        worksheet.setName(worksheetDetails.getName());
        return ResponseEntity.ok(worksheetRepository.save(worksheet));
    }

    @DeleteMapping("/worksheets/{id}")
    public ResponseEntity<Void> deleteWorksheet(@PathVariable Long id) {
        // A CascadeType.ALL miatt a JPA automatikusan törli a kapcsolódó AssetType és InvestmentSnapshot entitásokat is.
        worksheetRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/worksheets/{worksheetId}/asset-types")
    public List<AssetType> getAssetTypesForWorksheet(@PathVariable Long worksheetId) {
        return assetTypeRepository.findByWorksheetId(worksheetId);
    }

    @PostMapping("/worksheets/{worksheetId}/asset-types")
    public AssetType createAssetType(@PathVariable Long worksheetId, @RequestBody AssetType assetType) {
        Worksheet worksheet = worksheetRepository.findById(worksheetId)
                .orElseThrow(() -> new RuntimeException("Worksheet not found with id: " + worksheetId));
        assetType.setWorksheet(worksheet);
        return assetTypeRepository.save(assetType);
    }

    @PutMapping("/asset-types/{id}")
    public ResponseEntity<AssetType> updateAssetType(@PathVariable Long id, @RequestBody AssetType assetTypeDetails) {
        AssetType assetType = assetTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AssetType not found with id: " + id));
        assetType.setName(assetTypeDetails.getName());
        assetType.setColor(assetTypeDetails.getColor());
        return ResponseEntity.ok(assetTypeRepository.save(assetType));
    }

    @DeleteMapping("/asset-types/{id}")
    @Transactional
    public ResponseEntity<Void> deleteAssetType(@PathVariable Long id) {
        AssetType assetType = assetTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AssetType not found with id: " + id));

        List<InvestmentSnapshot> snapshots = investmentSnapshotRepository.findByWorksheetId(assetType.getWorksheet().getId());
        for (InvestmentSnapshot snapshot : snapshots) {
            List<AssetValue> updatedAssets = snapshot.getAssets().stream()
                    .filter(assetValue -> !assetValue.getAssetTypeId().equals(id))
                    .collect(Collectors.toList());
            snapshot.setAssets(updatedAssets);
            investmentSnapshotRepository.save(snapshot);
        }

        assetTypeRepository.delete(assetType);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/worksheets/{worksheetId}/snapshots")
    public List<InvestmentSnapshot> getSnapshotsForWorksheet(@PathVariable Long worksheetId) {
        return investmentSnapshotRepository.findByWorksheetId(worksheetId);
    }

    @PostMapping("/worksheets/{worksheetId}/snapshots")
    public InvestmentSnapshot createSnapshot(@PathVariable Long worksheetId, @RequestBody InvestmentSnapshot snapshot) {
        Worksheet worksheet = worksheetRepository.findById(worksheetId)
                .orElseThrow(() -> new RuntimeException("Worksheet not found with id: " + worksheetId));
        snapshot.setWorksheet(worksheet);
        return investmentSnapshotRepository.save(snapshot);
    }

    @PutMapping("/snapshots/{id}")
    public ResponseEntity<InvestmentSnapshot> updateSnapshot(@PathVariable Long id, @RequestBody InvestmentSnapshot snapshotDetails) {
        InvestmentSnapshot snapshot = investmentSnapshotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("InvestmentSnapshot not found with id: " + id));

        snapshot.setDate(snapshotDetails.getDate());
        snapshot.setAssets(snapshotDetails.getAssets());

        final InvestmentSnapshot updatedSnapshot = investmentSnapshotRepository.save(snapshot);
        return ResponseEntity.ok(updatedSnapshot);
    }

    @DeleteMapping("/snapshots/{id}")
    public ResponseEntity<Void> deleteSnapshot(@PathVariable Long id) {
        investmentSnapshotRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}