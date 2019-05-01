package com.quartet.inventorydemo.repository;

import com.quartet.inventorydemo.model.Requisition;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("RequisitionRepository")
public interface RequisitionRepository extends JpaRepository<Requisition, UUID> {

  default Set<Requisition> findAllToSet() {
    return new HashSet<>(findAll());
  }

  Optional<Requisition> getById(UUID uuid);
}
