package com.quartet.inventorydemo.repository;

import com.quartet.inventorydemo.model.InventoryPosition;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("InventoryPositionRepository")
public interface InventoryPositionRepository extends JpaRepository<InventoryPosition, UUID> {

  default Set<InventoryPosition> findAllToSet() {
    return new HashSet<>(findAll());
  }

  Optional<InventoryPosition> findByName(String name);

  Set<InventoryPosition> findByIdIn(Iterable<UUID> ids);
}
