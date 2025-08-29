package com.tomk99.nexusmaximusbackend.repositories;

import com.tomk99.nexusmaximusbackend.model.inventory.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}