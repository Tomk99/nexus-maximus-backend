package com.tomk99.nexusmaximusbackend.dto;

import java.time.LocalDate;

public record CreateRefuelingRequestDto(
        LocalDate date,
        int odometer,
        double liters
) {}