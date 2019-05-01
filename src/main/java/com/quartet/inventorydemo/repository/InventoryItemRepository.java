package com.quartet.inventorydemo.repository;

import com.quartet.inventorydemo.model.InventoryItem;
import com.quartet.inventorydemo.model.id.InventoryItemId;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("InventoryItemRepository")
public interface InventoryItemRepository extends JpaRepository<InventoryItem, InventoryItemId> {

  default Set<InventoryItem> findAllToSet() {
    return new HashSet<>(findAll());
  }
}
