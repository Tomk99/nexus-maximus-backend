package com.tomk99.nexusmaximusbackend.repositories;

import com.tomk99.nexusmaximusbackend.model.investment.AssetType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetTypeRepository extends JpaRepository<AssetType, Long> {
    List<AssetType> findByWorksheetId(Long worksheetId);
}