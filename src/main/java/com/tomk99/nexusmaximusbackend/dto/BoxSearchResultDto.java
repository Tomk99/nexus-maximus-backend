package com.tomk99.nexusmaximusbackend.dto;

import java.util.List;

public record BoxSearchResultDto(
        Long id,
        String name,
        String description,
        List<String> matchingItems
) {}