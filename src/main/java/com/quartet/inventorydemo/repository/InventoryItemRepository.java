package com.quartet.inventorydemo.repository;

import com.quartet.inventorydemo.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, UUID> {
    List<InventoryItem> findAll();

    Set<InventoryItem> findByInventoryItemIDIn(Iterable<UUID> ids);
    InventoryItem findByInventoryItemID(UUID inventoryItemID);
}
