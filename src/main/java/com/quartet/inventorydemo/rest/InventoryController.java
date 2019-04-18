package com.quartet.inventorydemo.rest;

import com.quartet.inventorydemo.model.InventoryItem;
import com.quartet.inventorydemo.model.InventoryPositionContents;
import com.quartet.inventorydemo.model.Requirement;
import com.quartet.inventorydemo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/inventory")
public class InventoryController {

    @Autowired
    @Qualifier("InventoryHolderService")
    private InventoryHolderService inventoryHolderService;

    @Autowired
    @Qualifier("InventoryPositionService")
    private InventoryPositionService inventoryPositionService;

    @Autowired
    @Qualifier("InventoryPositionContentsService")
    private InventoryPositionContentsService inventoryPositionContentsService;

    @Autowired
    @Qualifier("RequirementService")
    private RequirementService requirementService;

    @Autowired
    @Qualifier("Requirement_InventoryPositionService")
    private Requirement_InventoryPositionService requirement_inventoryPositionService;

    @Autowired
    @Qualifier("InventoryItemService")
    private InventoryItemService inventoryItemService;


}
