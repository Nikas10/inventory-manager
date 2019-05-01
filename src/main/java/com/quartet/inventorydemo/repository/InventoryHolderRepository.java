package com.quartet.inventorydemo.repository;

import com.quartet.inventorydemo.model.Holder;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("InventoryHolderRepository")
public interface InventoryHolderRepository extends JpaRepository<Holder, UUID> {

  default Set<Holder> findAllToSet() {
    return new HashSet<>(findAll());
  }

  Optional<Holder> findByName(String holderName);

  Set<Holder> findByIdIn(Iterable<UUID> ids);
}
