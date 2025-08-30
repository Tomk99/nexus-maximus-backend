package com.tomk99.nexusmaximusbackend.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.tomk99.nexusmaximusbackend.dto.BoxDetailDto;
import com.tomk99.nexusmaximusbackend.dto.BoxDto;
import com.tomk99.nexusmaximusbackend.dto.BoxSearchResultDto;
import com.tomk99.nexusmaximusbackend.dto.ItemDto;
import com.tomk99.nexusmaximusbackend.model.inventory.Box;
import com.tomk99.nexusmaximusbackend.model.inventory.Item;
import com.tomk99.nexusmaximusbackend.repositories.BoxRepository;
import com.tomk99.nexusmaximusbackend.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private final BoxRepository boxRepository;
    private final ItemRepository itemRepository;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public InventoryService(BoxRepository boxRepository, ItemRepository itemRepository) {
        this.boxRepository = boxRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional(readOnly = true)
    public List<BoxDto> getAllBoxes() {
        return boxRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BoxDetailDto getBoxById(Long boxId) {
        return boxRepository.findById(boxId)
                .map(this::toDetailDto)
                .orElseThrow(() -> new RuntimeException("Box not found with id: " + boxId));
    }

    @Transactional
    public BoxDto createBox(BoxDto boxDto) {
        Box box = new Box();
        box.setName(boxDto.name());
        box.setDescription(boxDto.description());
        Box savedBox = boxRepository.save(box);
        return toDto(savedBox);
    }

    @Transactional
    public BoxDto updateBox(Long boxId, BoxDto boxDto) {
        Box box = boxRepository.findById(boxId)
                .orElseThrow(() -> new RuntimeException("Box not found with id: " + boxId));
        box.setName(boxDto.name());
        box.setDescription(boxDto.description());
        Box updatedBox = boxRepository.save(box);
        return toDto(updatedBox);
    }

    @Transactional
    public void deleteBox(Long boxId) {
        boxRepository.deleteById(boxId);
    }

    @Transactional(readOnly = true)
    public List<BoxSearchResultDto> searchBoxesByItemName(String itemName) {
        if (itemName == null || itemName.isBlank()) {
            return getAllBoxes().stream()
                    .map(boxDto -> new BoxSearchResultDto(boxDto.id(), boxDto.name(), boxDto.description(), List.of()))
                    .collect(Collectors.toList());
        }

        List<Box> foundBoxes = boxRepository.findDistinctByItems_NameContainingIgnoreCase(itemName);
        String lowerCaseItemName = itemName.toLowerCase();

        return foundBoxes.stream()
                .map(box -> {
                    List<String> matchingItemNames = box.getItems().stream()
                            .filter(item -> item.getName().toLowerCase().contains(lowerCaseItemName))
                            .map(Item::getName)
                            .collect(Collectors.toList());

                    return new BoxSearchResultDto(
                            box.getId(),
                            box.getName(),
                            box.getDescription(),
                            matchingItemNames
                    );
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public ItemDto addItemToBox(Long boxId, ItemDto itemDto) {
        Box box = boxRepository.findById(boxId)
                .orElseThrow(() -> new RuntimeException("Box not found with id: " + boxId));
        Item item = new Item();
        item.setName(itemDto.name());
        item.setQuantity(itemDto.quantity());
        item.setBox(box);
        Item savedItem = itemRepository.save(item);
        return toDto(savedItem);
    }

    @Transactional
    public ItemDto updateItem(Long itemId, ItemDto itemDto) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + itemId));
        item.setName(itemDto.name());
        item.setQuantity(itemDto.quantity());
        Item updatedItem = itemRepository.save(item);
        return toDto(updatedItem);
    }

    @Transactional
    public void deleteItem(Long itemId) {
        itemRepository.deleteById(itemId);
    }

    @Transactional(readOnly = true)
    public byte[] generateQrCodeForBox(Long boxId) throws WriterException, IOException {
        Box box = boxRepository.findById(boxId)
                .orElseThrow(() -> new RuntimeException("Box not found with id: " + boxId));
        String urlToEncode = frontendUrl + "/inventory/box/" + boxId;
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(urlToEncode, BarcodeFormat.QR_CODE, 250, 250);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }

    private BoxDto toDto(Box box) {
        return new BoxDto(box.getId(), box.getName(), box.getDescription());
    }

    private ItemDto toDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getQuantity());
    }

    private BoxDetailDto toDetailDto(Box box) {
        List<ItemDto> itemDtos = box.getItems().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return new BoxDetailDto(box.getId(), box.getName(), box.getDescription(), itemDtos);
    }
}