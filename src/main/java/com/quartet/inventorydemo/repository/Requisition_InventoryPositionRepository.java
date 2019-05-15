package com.quartet.inventorydemo.repository;

import com.quartet.inventorydemo.model.Requisition_InventoryPosition;
import com.quartet.inventorydemo.model.id.Requisition_InventoryPositionId;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("Requisition_InventoryPositionRepository")
public interface Requisition_InventoryPositionRepository
    extends JpaRepository<Requisition_InventoryPosition, Requisition_InventoryPositionId> {

  default Set<Requisition_InventoryPosition> findAllToSet() {
    return new HashSet<>(findAll());
  }

  Optional<Requisition_InventoryPosition> findByInventoryPosition_IdAndRequisition_Id(UUID position, UUID requisition);

}
