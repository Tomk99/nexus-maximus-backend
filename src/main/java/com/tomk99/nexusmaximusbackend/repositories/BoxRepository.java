package com.tomk99.nexusmaximusbackend.repositories;

import com.tomk99.nexusmaximusbackend.model.inventory.Box;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoxRepository extends JpaRepository<Box, Long> {
}