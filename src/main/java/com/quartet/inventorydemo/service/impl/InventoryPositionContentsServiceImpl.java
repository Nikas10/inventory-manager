package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.model.InventoryPositionContents;
import com.quartet.inventorydemo.repository.InventoryPositionContentsRepository;
import com.quartet.inventorydemo.repository.InventoryPositionRepository;
import com.quartet.inventorydemo.service.InventoryPositionContentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("InventoryPositionContentsService")
public class InventoryPositionContentsServiceImpl implements InventoryPositionContentsService {

    @Autowired
    InventoryPositionContentsRepository inventoryPositionContentsRepository;


    @Override
    public int getAmount(InventoryPosition bundle, InventoryPosition partOfInventoryPosition) {
        InventoryPositionContents inventoryPositionContents = inventoryPositionContentsRepository
                .findByBundleAndPartOfInventoryPosition(bundle, partOfInventoryPosition);

        return inventoryPositionContents.getAmount();
    }

    @Override
    public InventoryPositionContents change(InventoryPosition bundle, InventoryPosition partOfInventoryPosition, int value) {
        InventoryPositionContents inventoryPositionContents = inventoryPositionContentsRepository
                .findByBundleAndPartOfInventoryPosition(bundle, partOfInventoryPosition);

        if (inventoryPositionContents == null) {
            inventoryPositionContents = new InventoryPositionContents(value, bundle, partOfInventoryPosition);
        } else {
            inventoryPositionContents.setAmount(value);
        }

        return inventoryPositionContentsRepository.saveAndFlush(inventoryPositionContents);
    }
}
