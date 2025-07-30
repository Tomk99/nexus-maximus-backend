package com.tomk99.nexusmaximusbackend.model.investment;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetValue {
    private Long assetTypeId;
    private String name;
    private String color;
    private double value;
}