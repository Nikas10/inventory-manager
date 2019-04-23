package com.quartet.inventorydemo.repository;

import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.model.Requirement;
import com.quartet.inventorydemo.model.Requirement_InventoryPosition;
import com.quartet.inventorydemo.model.id.RequirInvPosID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Requirement_InventoryPositionRepository extends JpaRepository<Requirement_InventoryPosition, RequirInvPosID> {

    List<Requirement_InventoryPosition> findAll();
    Requirement_InventoryPosition findByRequirementAndInventoryPosition(Requirement requirement, InventoryPosition position);
    List<Requirement_InventoryPosition> findByRequirement(Requirement requirement);
    List<Requirement_InventoryPosition> findByInventoryPosition(InventoryPosition position);
}
