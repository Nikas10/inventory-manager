package com.quartet.inventorydemo.repository;

import com.quartet.inventorydemo.model.Requirement;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("RequirementRepository")
public interface RequirementRepository extends JpaRepository<Requirement, UUID> {

  default Set<Requirement> findAllToSet() {
    return new HashSet<>(findAll());
  }

  Optional<Requirement> findByName(String name);
}
