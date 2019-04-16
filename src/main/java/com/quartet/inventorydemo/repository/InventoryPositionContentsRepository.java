package com.quartet.inventorydemo.repository;

import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.model.InventoryPositionContents;
import com.quartet.inventorydemo.model.id.InventoryPositionContentsID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryPositionContentsRepository extends JpaRepository<InventoryPositionContents, InventoryPositionContentsID> {

    List<InventoryPositionContents> findAll();
    InventoryPositionContents findByBundleAndPartOfInventoryPosition(InventoryPosition bundle, InventoryPosition partOfInventoryPosition);
}
