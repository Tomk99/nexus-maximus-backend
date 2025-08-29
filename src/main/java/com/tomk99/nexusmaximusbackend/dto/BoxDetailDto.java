package com.tomk99.nexusmaximusbackend.dto;

import java.util.List;

public record BoxDetailDto(
        Long id,
        String name,
        String description,
        List<ItemDto> items
) {}