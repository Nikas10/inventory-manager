package com.quartet.inventorydemo.repository;

import com.quartet.inventorydemo.model.InventoryHolder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface InventoryHolderRepository extends JpaRepository<InventoryHolder, UUID> {
    List<InventoryHolder> findAll();

    Set<InventoryHolder> findByHolderIDIn(Iterable<UUID> ids);
    InventoryHolder findByHolderID(UUID holderID);

    List<InventoryHolder> findByName(String holderName);
}
