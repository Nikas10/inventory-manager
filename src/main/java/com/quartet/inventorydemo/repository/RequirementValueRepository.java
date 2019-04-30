package com.quartet.inventorydemo.repository;

import com.quartet.inventorydemo.model.RequirementValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("RequirementValueRepository")
public interface RequirementValueRepository extends JpaRepository<RequirementValue, RequirementValue.RequirementValueID> {
    List<RequirementValue> findAll();

    Optional<RequirementValue> findByRequirement_IdAndInventoryPosition_Id(UUID requirementId, UUID inventoryPositionId);
}
