package com.quartet.inventorydemo.repository;

import com.quartet.inventorydemo.model.InventoryPosition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InventoryPositionRepository extends JpaRepository<InventoryPosition, UUID> {
    List<InventoryPosition> findAll();
    InventoryPosition findByPositionID(UUID positionID);
}
