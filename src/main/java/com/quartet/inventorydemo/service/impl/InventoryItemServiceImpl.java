package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.model.InventoryItem;
import com.quartet.inventorydemo.repository.InventoryItemRepository;
import com.quartet.inventorydemo.service.InventoryItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("InventoryItemService")
@Transactional
public class InventoryItemServiceImpl implements InventoryItemService {
    @Autowired
    private InventoryItemRepository invItemRepo;

    @Override
    public List<InventoryItem> getAll() {
        return invItemRepo.findAll();
    }

    @Override
    public InventoryItem add(InventoryItem item) {
        return invItemRepo.saveAndFlush(item);
    }

    @Override
    public InventoryItem update(InventoryItem item) {
        return invItemRepo.saveAndFlush(item);
    }
}
