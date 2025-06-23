package com.tomk99.nexusmaximusbackend.model.investment;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Worksheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "worksheet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssetType> assetTypes;

    @OneToMany(mappedBy = "worksheet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvestmentSnapshot> snapshots;
}