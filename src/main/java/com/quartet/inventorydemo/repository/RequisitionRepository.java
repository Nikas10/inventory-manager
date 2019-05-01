package com.quartet.inventorydemo.repository;

import com.quartet.inventorydemo.model.Requisition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository("RequisitionRepository")
public interface RequisitionRepository extends JpaRepository<Requisition, UUID> {
    default Set<Requisition> findAllToSet() {
        return new HashSet<>(findAll());
    }
    Optional<Requisition> getById(UUID uuid);
}
