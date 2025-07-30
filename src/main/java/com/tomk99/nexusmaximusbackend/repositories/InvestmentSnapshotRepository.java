package com.tomk99.nexusmaximusbackend.repositories;

import com.tomk99.nexusmaximusbackend.model.investment.InvestmentSnapshot;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvestmentSnapshotRepository extends JpaRepository<InvestmentSnapshot, Long> {
    @EntityGraph(attributePaths = {"assets"})
    List<InvestmentSnapshot> findByWorksheetId(Long worksheetId);
    @Modifying
    @Query(value = "DELETE FROM snapshot_assets WHERE asset_type_id = ?1", nativeQuery = true)
    void deleteAssetValuesByAssetTypeId(Long assetTypeId);
}