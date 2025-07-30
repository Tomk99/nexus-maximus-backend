package com.tomk99.nexusmaximusbackend.dto;

public record AssetValue(
        Long assetTypeId,
        String name,
        String color,
        double value
) {}