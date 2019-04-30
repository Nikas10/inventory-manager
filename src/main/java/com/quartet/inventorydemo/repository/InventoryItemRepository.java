package com.quartet.inventorydemo.repository;

import com.quartet.inventorydemo.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

@Repository("InventoryItemRepository")
public interface InventoryItemRepository extends JpaRepository<InventoryItem, InventoryItem.InventoryItemId> {
    default Set<InventoryItem> findAllToSet() {
        return new HashSet<>(findAll());
    }
}
