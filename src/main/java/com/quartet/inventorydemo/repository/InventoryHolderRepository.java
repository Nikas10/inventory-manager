package com.quartet.inventorydemo.repository;

import com.quartet.inventorydemo.model.Account;
import com.quartet.inventorydemo.model.InventoryHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface InventoryHolderRepository extends JpaRepository<InventoryHolder, UUID> {
    List<InventoryHolder> findAll();
    Set<InventoryHolder> findByHolderIDIn(Iterable<UUID> ids);
    InventoryHolder findByHolderID(UUID holderID);
    List<InventoryHolder> findByName(String holderName);

    @Query(
        value = "SELECT * FROM  inventory_holder " +
                "JOIN account_inventory_holder acc_inv " +
                "ON acc_inv.holderid = inventory_holder.holderid",
        nativeQuery = true)
    List<Account> findHolderWithAccount();
}
