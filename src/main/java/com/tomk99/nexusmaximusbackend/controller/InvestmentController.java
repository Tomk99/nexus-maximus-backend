package com.tomk99.nexusmaximusbackend.controller;

import com.tomk99.nexusmaximusbackend.dto.AssetTypeDto;
import com.tomk99.nexusmaximusbackend.dto.InvestmentSnapshotDto;
import com.tomk99.nexusmaximusbackend.dto.WorksheetDto;
import com.tomk99.nexusmaximusbackend.service.AssetTypeService;
import com.tomk99.nexusmaximusbackend.service.InvestmentService;
import com.tomk99.nexusmaximusbackend.service.WorksheetService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class InvestmentController {

    private final InvestmentService investmentService;
    private final WorksheetService worksheetService;
    private final AssetTypeService assetTypeService;

    public InvestmentController(InvestmentService investmentService,
                                WorksheetService worksheetService,
                                AssetTypeService assetTypeService) {
        this.investmentService = investmentService;
        this.worksheetService = worksheetService;
        this.assetTypeService = assetTypeService;
    }

    @GetMapping("/investments/export/csv")
    public void exportInvestmentDataToCsv(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/csv; charset=UTF-8");
        response.getWriter().write('\uFEFF'); // BOM az Excel kompatibilitásért
        response.setHeader("Content-Disposition", "attachment; filename=\"investment_data.csv\"");
        investmentService.exportInvestmentDataToCsv(response.getWriter());
    }

    @GetMapping("/worksheets")
    public List<WorksheetDto> getAllWorksheets() {
        return worksheetService.getAllWorksheets();
    }

    @PostMapping("/worksheets")
    @ResponseStatus(HttpStatus.CREATED)
    public WorksheetDto createWorksheet(@RequestBody WorksheetDto worksheetDto) {
        return worksheetService.createWorksheet(worksheetDto);
    }

    @PutMapping("/worksheets/{id}")
    public WorksheetDto updateWorksheet(@PathVariable Long id, @RequestBody WorksheetDto worksheetDetails) {
        return worksheetService.updateWorksheet(id, worksheetDetails);
    }

    @DeleteMapping("/worksheets/{id}")
    public ResponseEntity<Void> deleteWorksheet(@PathVariable Long id) {
        worksheetService.deleteWorksheet(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/worksheets/{worksheetId}/asset-types")
    public List<AssetTypeDto> getAssetTypesForWorksheet(@PathVariable Long worksheetId) {
        return assetTypeService.getAssetTypesForWorksheet(worksheetId);
    }

    @PostMapping("/worksheets/{worksheetId}/asset-types")
    @ResponseStatus(HttpStatus.CREATED)
    public AssetTypeDto createAssetType(@PathVariable Long worksheetId, @RequestBody AssetTypeDto assetTypeDto) {
        return assetTypeService.createAssetType(worksheetId, assetTypeDto);
    }

    @PutMapping("/asset-types/{id}")
    public AssetTypeDto updateAssetType(@PathVariable Long id, @RequestBody AssetTypeDto assetTypeDetails) {
        return assetTypeService.updateAssetType(id, assetTypeDetails);
    }

    @DeleteMapping("/asset-types/{id}")
    public ResponseEntity<Void> deleteAssetType(@PathVariable Long id) {
        investmentService.deleteAssetTypeAndCleanSnapshots(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/worksheets/{worksheetId}/snapshots")
    public List<InvestmentSnapshotDto> getSnapshotsForWorksheet(@PathVariable Long worksheetId) {
        return investmentService.getSnapshotsForWorksheet(worksheetId);
    }

    @PostMapping("/worksheets/{worksheetId}/snapshots")
    @ResponseStatus(HttpStatus.CREATED)
    public InvestmentSnapshotDto createSnapshot(@PathVariable Long worksheetId, @RequestBody InvestmentSnapshotDto snapshotDto) {
        return investmentService.createSnapshot(worksheetId, snapshotDto);
    }

    @PutMapping("/snapshots/{id}")
    public InvestmentSnapshotDto updateSnapshot(@PathVariable Long id, @RequestBody InvestmentSnapshotDto snapshotDetails) {
        return investmentService.updateSnapshot(id, snapshotDetails);
    }

    @DeleteMapping("/snapshots/{id}")
    public ResponseEntity<Void> deleteSnapshot(@PathVariable Long id) {
        investmentService.deleteSnapshot(id);
        return ResponseEntity.noContent().build();
    }
}