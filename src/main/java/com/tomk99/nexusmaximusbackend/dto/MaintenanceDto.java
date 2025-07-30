package com.tomk99.nexusmaximusbackend.dto;

import java.time.LocalDate;

public record MaintenanceDto(
        Long id,
        LocalDate date,
        Integer odometer,
        String description
) {}