package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.model.InventoryHolder;
import com.quartet.inventorydemo.repository.InventoryHolderRepository;
import com.quartet.inventorydemo.service.InventoryHolderService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

public class InventoryHolderServiceImpl implements InventoryHolderService {

    @Autowired
    InventoryHolderRepository invHoldRepo;

    @Override
    public List<InventoryHolder> getAll() {
        return invHoldRepo.findAll();
    }

    @Override
    public InventoryHolder getByHolderID(UUID holderID) {
        return invHoldRepo.findByHolderID(holderID);
    }

    @Override
    public InventoryHolder add(InventoryHolder holder) {
        holder.setHolderID(UUID.randomUUID());
        return invHoldRepo.saveAndFlush(holder);
    }

    @Override
    public InventoryHolder update(InventoryHolder holder) {
        return invHoldRepo.saveAndFlush(holder);
    }
}
