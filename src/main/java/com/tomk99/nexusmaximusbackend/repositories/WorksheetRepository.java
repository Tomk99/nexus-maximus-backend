package com.tomk99.nexusmaximusbackend.repositories;

import com.tomk99.nexusmaximusbackend.model.investment.Worksheet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorksheetRepository extends JpaRepository<Worksheet, Long> {}