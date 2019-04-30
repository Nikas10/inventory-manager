package com.quartet.inventorydemo.repository;

import com.quartet.inventorydemo.model.InventoryPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository("InventoryPositionRepository")
public interface InventoryPositionRepository extends JpaRepository<InventoryPosition, UUID> {
    default Set<InventoryPosition> findAllToSet() {
        return new HashSet<>(findAll());
    }

    Optional<InventoryPosition> findByName(String name);

    Optional<InventoryPosition> findById(UUID positionID);
    Set<InventoryPosition> findByIdIn(Iterable<UUID> ids);
}
