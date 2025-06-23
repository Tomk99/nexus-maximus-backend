package com.tomk99.nexusmaximusbackend.repositories;

import com.tomk99.nexusmaximusbackend.model.investment.InvestmentSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvestmentSnapshotRepository extends JpaRepository<InvestmentSnapshot, Long> {
    List<InvestmentSnapshot> findByWorksheetId(Long worksheetId);
}