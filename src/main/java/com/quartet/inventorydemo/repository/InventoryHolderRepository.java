package com.quartet.inventorydemo.repository;

import com.quartet.inventorydemo.model.Holder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface InventoryHolderRepository extends JpaRepository<Holder, UUID> {
    List<Holder> findAll();

    Optional<Holder> findByName(String holderName);

    Set<Holder> findByIdIn(Iterable<UUID> ids);
}
