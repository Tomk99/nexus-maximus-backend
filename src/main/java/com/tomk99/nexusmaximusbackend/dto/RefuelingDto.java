package com.tomk99.nexusmaximusbackend.dto;

import java.time.LocalDate;

public record RefuelingDto(
        Long id,
        LocalDate date,
        int odometer,
        double liters
) {}