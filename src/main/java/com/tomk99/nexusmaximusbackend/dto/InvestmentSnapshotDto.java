package com.tomk99.nexusmaximusbackend.dto;

import java.time.LocalDate;
import java.util.List;

public record InvestmentSnapshotDto(
        Long id,
        LocalDate date,
        List<AssetValueDto> assets
) {}