package com.quartet.inventorydemo.repository;

import com.quartet.inventorydemo.model.Requisition_InventoryPosition;
import com.quartet.inventorydemo.model.id.Requisition_InventoryPositionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

@Repository("Requisition_InventoryPositionRepository")
public interface Requisition_InventoryPositionRepository extends JpaRepository<Requisition_InventoryPosition, Requisition_InventoryPositionId> {
    default Set<Requisition_InventoryPosition> findAllToSet() {
        return new HashSet<>(findAll());
    }
}
