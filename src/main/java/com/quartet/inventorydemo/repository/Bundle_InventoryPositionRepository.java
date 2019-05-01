package com.quartet.inventorydemo.repository;

import com.quartet.inventorydemo.model.Bundle_InventoryPosition;
import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.model.id.Bundle_InventoryPositionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("Bundle_InventoryPositionRepository")
public interface Bundle_InventoryPositionRepository extends JpaRepository<Bundle_InventoryPosition, Bundle_InventoryPositionId> {
    Bundle_InventoryPosition findByInventoryPositionAndBundlePosition(InventoryPosition inventoryPosition, InventoryPosition bundlePosition);

    List<Bundle_InventoryPosition> findByInventoryPosition(InventoryPosition inventoryPosition);
}
