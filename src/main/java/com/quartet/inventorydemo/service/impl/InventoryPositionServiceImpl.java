package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.model.Bundle_InventoryPosition;
import com.quartet.inventorydemo.model.Holder;
import com.quartet.inventorydemo.model.InventoryItem;
import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.repository.Bundle_InventoryPositionRepository;
import com.quartet.inventorydemo.repository.InventoryHolderRepository;
import com.quartet.inventorydemo.repository.InventoryItemRepository;
import com.quartet.inventorydemo.repository.InventoryPositionRepository;
import com.quartet.inventorydemo.service.InventoryPositionService;
import com.quartet.inventorydemo.util.OnCreate;
import com.quartet.inventorydemo.util.OnUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;

@Service("InventoryPositionService")
@Validated
@org.springframework.transaction.annotation.Transactional
public class InventoryPositionServiceImpl implements InventoryPositionService {
    private final InventoryPositionRepository positionRepo;
    private final InventoryItemRepository inventoryItemRepo;
    private final InventoryHolderRepository inventoryHolderRepo;
    private final Bundle_InventoryPositionRepository Bundle_InventoryPositionRepo;

    @Autowired
    public InventoryPositionServiceImpl(@Qualifier("InventoryPositionRepository") final InventoryPositionRepository positionRepo,
                                        @Qualifier("InventoryItemRepository") final InventoryItemRepository inventoryItemRepo,
                                        @Qualifier("InventoryHolderRepository") final InventoryHolderRepository inventoryHolderRepo,
                                        @Qualifier("Bundle_InventoryPositionRepository") final Bundle_InventoryPositionRepository Bundle_InventoryPositionRepo
    ) {
        this.positionRepo = positionRepo;
        this.inventoryItemRepo = inventoryItemRepo;
        this.inventoryHolderRepo = inventoryHolderRepo;
        this.Bundle_InventoryPositionRepo = Bundle_InventoryPositionRepo;
    }

    @Override
    public List<InventoryPosition> getAll() {
        return positionRepo.findAll();
    }

    @Override
    public InventoryPosition getByPositionID(@NotNull @Valid UUID positionID) {
        Optional<InventoryPosition> byId = positionRepo.findById(positionID);
        byId.orElseThrow(ResourceNotFoundException::new);
        return byId.get();
    }

    @Override
    public InventoryPosition getByName(@NotBlank @Valid String name) {
        Optional<InventoryPosition> byName = positionRepo.findByName(name);
        byName.orElseThrow(ResourceNotFoundException::new);
        return byName.get();
    }

    @Override
    public Set<InventoryPosition> getByPositionIDs(@NotNull @Valid Set<UUID> positionIDs) {
        return positionRepo.findByIdIn(positionIDs);
    }

    @Validated(OnCreate.class)
    @Override
    public InventoryPosition add(@NotNull @Valid InventoryPosition position) {
        return positionRepo.saveAndFlush(position);
    }

    @Validated(OnUpdate.class)
    @Override
    public InventoryPosition update(@NotNull @Valid InventoryPosition position) {
        return positionRepo.saveAndFlush(position);
    }

    @Validated(OnUpdate.class)
    @Override
    public void remove(@NotNull @Valid InventoryPosition inventoryPosition) {
        if (inventoryPosition.getBundleInventoryPositions() != null) {
            //inventoryPosition is bundle
            removeBundle(inventoryPosition);
        } else {
            //inventoryPosition is not bundle
            removeInventoryPosition(inventoryPosition);
        }
    }

    private void removeInventoryPosition(InventoryPosition position) {
        Optional<InventoryPosition> itemsWithSelectedPosition = positionRepo.findById(position.getId());

        List<Bundle_InventoryPosition> bundlesWithThisPosition = Bundle_InventoryPositionRepo.findByInventoryPosition(position);

        for (Bundle_InventoryPosition bundle : bundlesWithThisPosition) {
            Bundle_InventoryPositionRepo.delete(bundle);
        }

        if (itemsWithSelectedPosition == null) {
            positionRepo.delete(position);
            return;
        }
    }

    private void removeBundle(InventoryPosition position) {
        //check if everyone in storage else throw exception
        Set<Bundle_InventoryPosition> bundleInventoryPositions = position.getBundleInventoryPositions();
        List<InventoryItem> allItems = new ArrayList<>();
        Holder stockHolder = inventoryHolderRepo.findByName("stock").get();
        for (Bundle_InventoryPosition partOfBundle : bundleInventoryPositions) {
            Integer amount = partOfBundle.getAmount();
            InventoryPosition partOfInventoryPosition = partOfBundle.getInventoryPosition();
            Set<InventoryItem> currentPositionItems = partOfInventoryPosition.getInventoryItems();
            boolean existsInStorage = false;
            for (InventoryItem inventoryItem : currentPositionItems) {
                Holder inventoryHolder = inventoryItem.getHolder();
                if (stockHolder.equals(inventoryHolder)) {
                    existsInStorage = true;
                    inventoryItem.setAmount(amount + inventoryItem.getAmount());
                    break;
                }
            }
            if (!existsInStorage) {

                InventoryItem inventoryItem = new InventoryItem(stockHolder, partOfInventoryPosition, "On stock", amount);
                //repo inventory item save this shit
                allItems.add(inventoryItem);
            }
        }
        inventoryItemRepo.saveAll(allItems);
        positionRepo.delete(position); //если нет каскадного удаления, то удалить отдельно inventory item и хуйню

    }
}
