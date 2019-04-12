package com.quartet.inventorydemo.service.report.impl;

import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.service.InventoryPositionService;
import com.quartet.inventorydemo.service.report.InventoryPositionReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("InventoryPositionReportService")
public class InventoryPositionReportServiceImpl implements InventoryPositionReportService {

    @Autowired
    InventoryPositionService inventoryPositionService;

    @Override
    public List<Map<String, Object>> report() {
        inventoryPositionService.add(new InventoryPosition(UUID.randomUUID(), "name1"));
        List<InventoryPosition> inventoryPositions = inventoryPositionService.getAll();
        List<Map<String, Object>> result = new ArrayList<>();
        for (InventoryPosition inventoryPosition : inventoryPositions) {
            Map<String, Object> stringObjectMap = new HashMap<>();
            stringObjectMap.put("positionID", inventoryPosition.getPositionID());
            stringObjectMap.put("name", inventoryPosition.getName());
            result.add(stringObjectMap);
        }

        return result;
    }
}
