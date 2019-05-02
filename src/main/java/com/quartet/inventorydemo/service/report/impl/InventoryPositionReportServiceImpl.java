package com.quartet.inventorydemo.service.report.impl;

import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.service.InventoryPositionService;
import com.quartet.inventorydemo.service.report.InventoryPositionReportService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("InventoryPositionReportService")
public class InventoryPositionReportServiceImpl implements InventoryPositionReportService {

  @Autowired private InventoryPositionService inventoryPositionService;

  @Override
  public List<Map<String, Object>> report() {
    List<InventoryPosition> inventoryPositions = new ArrayList<>(inventoryPositionService.getAll());
    List<Map<String, Object>> result = new ArrayList<>();
    for (InventoryPosition inventoryPosition : inventoryPositions) {
      Map<String, Object> stringObjectMap = new HashMap<>();
      stringObjectMap.put("positionID", inventoryPosition.getId());
      stringObjectMap.put("name", inventoryPosition.getName());
      result.add(stringObjectMap);
    }

    return result;
  }
}
