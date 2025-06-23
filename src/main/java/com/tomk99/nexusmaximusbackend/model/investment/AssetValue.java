package com.tomk99.nexusmaximusbackend.model.investment;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class AssetValue {
    private Long assetTypeId;
    private String name;
    private String color;
    private double value;
}