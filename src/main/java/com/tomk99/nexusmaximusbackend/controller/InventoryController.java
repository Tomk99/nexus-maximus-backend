package com.tomk99.nexusmaximusbackend.controller;

import com.google.zxing.WriterException;
import com.tomk99.nexusmaximusbackend.dto.BoxDetailDto;
import com.tomk99.nexusmaximusbackend.dto.BoxDto;
import com.tomk99.nexusmaximusbackend.dto.BoxSearchResultDto;
import com.tomk99.nexusmaximusbackend.dto.ItemDto;
import com.tomk99.nexusmaximusbackend.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/boxes")
    public List<BoxDto> getAllBoxes() {
        return inventoryService.getAllBoxes();
    }

    @GetMapping("/boxes/search")
    public List<BoxSearchResultDto> searchBoxes(@RequestParam(required = false, defaultValue = "") String itemName) {
        return inventoryService.searchBoxesByItemName(itemName);
    }

    @GetMapping("/boxes/{id}")
    public BoxDetailDto getBoxById(@PathVariable Long id) {
        return inventoryService.getBoxById(id);
    }

    @PostMapping("/boxes")
    @ResponseStatus(HttpStatus.CREATED)
    public BoxDto createBox(@RequestBody BoxDto boxDto) {
        return inventoryService.createBox(boxDto);
    }

    @PutMapping("/boxes/{id}")
    public BoxDto updateBox(@PathVariable Long id, @RequestBody BoxDto boxDto) {
        return inventoryService.updateBox(id, boxDto);
    }

    @DeleteMapping("/boxes/{id}")
    public ResponseEntity<Void> deleteBox(@PathVariable Long id) {
        inventoryService.deleteBox(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/boxes/{boxId}/items")
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto addItemToBox(@PathVariable Long boxId, @RequestBody ItemDto itemDto) {
        return inventoryService.addItemToBox(boxId, itemDto);
    }

    @PutMapping("/items/{id}")
    public ItemDto updateItem(@PathVariable Long id, @RequestBody ItemDto itemDto) {
        return inventoryService.updateItem(id, itemDto);
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        inventoryService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/boxes/{id}/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getQrCodeForBox(@PathVariable Long id) {
        try {
            byte[] qrCodeImage = inventoryService.generateQrCodeForBox(id);
            return ResponseEntity.ok(qrCodeImage);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}