package com.quartet.inventorydemo.repository;

import com.quartet.inventorydemo.model.RequirementValue;
import com.quartet.inventorydemo.model.id.RequirementValueId;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("RequirementValueRepository")
public interface RequirementValueRepository
    extends JpaRepository<RequirementValue, RequirementValueId> {

  Optional<RequirementValue> findByRequirement_IdAndInventoryPosition_Id(
      UUID requirementId, UUID inventoryPositionId);
}
