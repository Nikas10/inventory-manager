package com.quartet.inventorydemo.repository;

import com.quartet.inventorydemo.model.InventoryHolder;
import com.quartet.inventorydemo.model.InventoryItem;
import com.quartet.inventorydemo.model.InventoryPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, UUID> {
    List<InventoryItem> findAll();

    Set<InventoryItem> findByInventoryItemIDIn(Iterable<UUID> ids);
    InventoryItem findByInventoryItemID(UUID inventoryItemID);

    @Query("SELECT item from InventoryItem item where item.inventoryPosition =:position")
    List<InventoryItem> findByInventoryPosition(@Param("position") InventoryPosition position);

    @Query("SELECT item from InventoryItem item where item.inventoryPosition =:position and item.inventoryHolder =:holder")
    InventoryItem findByInventoryPositionAndHolderID(@Param("position") InventoryPosition position, @Param("holder") InventoryHolder holder);
}
