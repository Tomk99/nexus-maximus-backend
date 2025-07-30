package com.tomk99.nexusmaximusbackend.dto;

public record AssetValueDto(
        Long assetTypeId,
        String name,
        String color,
        double value
) {}