package com.quartet.inventorydemo.repository;

import com.quartet.inventorydemo.model.InventoryHolder;
import com.quartet.inventorydemo.model.InventoryPosition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InventoryHolderRepository extends JpaRepository<InventoryHolder, UUID> {
    List<InventoryHolder> findAll();
    InventoryHolder findByHolderID(UUID holderID);
}
