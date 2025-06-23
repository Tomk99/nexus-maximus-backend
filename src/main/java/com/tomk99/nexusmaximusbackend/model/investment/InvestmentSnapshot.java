package com.tomk99.nexusmaximusbackend.model.investment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class InvestmentSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @ElementCollection
    @CollectionTable(name = "snapshot_assets", joinColumns = @JoinColumn(name = "snapshot_id"))
    private List<AssetValue> assets;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worksheet_id")
    @JsonIgnore
    private Worksheet worksheet;
}