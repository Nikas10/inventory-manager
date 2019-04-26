package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.model.InventoryHolder;
import com.quartet.inventorydemo.model.InventoryItem;
import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.model.InventoryPositionContents;
import com.quartet.inventorydemo.repository.InventoryHolderRepository;
import com.quartet.inventorydemo.repository.InventoryItemRepository;
import com.quartet.inventorydemo.repository.InventoryPositionContentsRepository;
import com.quartet.inventorydemo.repository.InventoryPositionRepository;
import com.quartet.inventorydemo.service.InventoryPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.Position;
import javax.transaction.Transactional;
import java.util.*;

@Service("InventoryPositionService")
@Transactional
public class InventoryPositionServiceImpl implements InventoryPositionService {

    @Autowired
    private InventoryPositionRepository positionRepo;

    @Autowired
    private InventoryItemRepository inventoryItemRepo;

    @Autowired
    private InventoryHolderRepository inventoryHolderRepo;

    @Autowired
    private InventoryPositionContentsRepository inventoryPositionContentsRepo;

    @Override
    public List<InventoryPosition> getAll() {
        return positionRepo.findAll();
    }

    @Override
    public Set<InventoryPosition> getByPositionIDs(Set<UUID> positionIDs) {
        return positionRepo.findByPositionIDIn(positionIDs);
    }

    @Override
    public InventoryPosition getByPositionID(UUID positionID) {
        return positionRepo.findByPositionID(positionID);
    }

    @Override
    public InventoryPosition getByName(String name) {
        return positionRepo.findByName(name);
    }

    @Override
    public InventoryPosition add(InventoryPosition position) {
        position.setPositionID(UUID.randomUUID());
        return positionRepo.saveAndFlush(position);
    }

    @Override
    public InventoryPosition update(InventoryPosition position) {
        return positionRepo.saveAndFlush(position);
    }

    @Override
    public void removeInventoryItem(InventoryPosition position) {
        List<InventoryItem> itemsWithSelectedPosition = inventoryItemRepo.findByInventoryPosition(position);

        List<InventoryPositionContents> bundlesWithThisPosition = inventoryPositionContentsRepo.findByPartOfInventoryPosition(position);

        for (InventoryPositionContents bundle: bundlesWithThisPosition) {
            inventoryPositionContentsRepo.delete(bundle);
        }

        if (itemsWithSelectedPosition == null) {
            positionRepo.delete(position);
            return;
        }
    }

    @Override
    public void removeBundle(InventoryPosition position) {
        Set<InventoryItem> currentTypeItems = position.getCurrentTypeItems();
        //check if everyone in storage else throw exception
        Set<InventoryPositionContents> partOfBundles = position.getPartOfBundles();
        List<InventoryItem> allItems = new ArrayList<>();
        for (InventoryPositionContents partOfBundle : partOfBundles) {
            Integer amount = partOfBundle.getAmount();
            InventoryPosition partOfInventoryPosition = partOfBundle.getPartOfInventoryPosition();
            Set<InventoryItem> currentTypeItems1 = partOfInventoryPosition.getCurrentTypeItems();
            boolean existsInStorage = false;
            for (InventoryItem inventoryItem : currentTypeItems1) {
                InventoryHolder inventoryHolder = inventoryItem.getInventoryHolder();
                //if holder = storage
                existsInStorage = true;
                inventoryItem.setAmount(amount + inventoryItem.getAmount());
                break;
            }
            if (!existsInStorage) {
                InventoryItem inventoryItem = new InventoryItem();
                inventoryItem.setInventoryHolder(/*storage holder*/ new InventoryHolder());
                inventoryItem.setInventoryPosition(partOfInventoryPosition);
                inventoryItem.setAmount(amount);
                //repo inventory item save this shit
                allItems.add(inventoryItem);

            }
        }
        inventoryItemRepo.saveAll(allItems);
        positionRepo.delete(position); //если нет каскадного удаления, то удалить отдельно inventory item и хуйню

    }

    private List<InventoryPositionContents> findParents(InventoryPosition position){
        return inventoryPositionContentsRepo.findByPartOfInventoryPosition(position);
    }

    private List<InventoryPositionContents> createParentsTree(InventoryPosition bundle) {
        List<InventoryPositionContents> allParents = new ArrayList<>();

        int i = 0;
        while (true) {
            if (i == allParents.size()) {
                break;
            }
            allParents.addAll(findParents(bundle));
            bundle = allParents.get(i).getBundle();
            i++;
        }

        return allParents;
    }
}
