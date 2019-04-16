package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.model.Requirement;
import com.quartet.inventorydemo.model.Requirement_InventoryPosition;
import com.quartet.inventorydemo.repository.InventoryPositionRepository;
import com.quartet.inventorydemo.repository.RequirementRepository;
import com.quartet.inventorydemo.repository.Requirement_InventoryPositionRepository;
import com.quartet.inventorydemo.service.Requirement_InventoryPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service("Requirement_InventoryPositionService")
@Transactional
public class Requirement_InventoryPositionServiceImpl implements Requirement_InventoryPositionService {

    @Autowired
    InventoryPositionRepository positionRepo;

    @Autowired
    RequirementRepository requirementRepo;

    @Autowired
    Requirement_InventoryPositionRepository requir_invPosRepo;

    @Override
    public Requirement_InventoryPosition changeRequirement(UUID positionID, UUID requirementID, String value) {
        InventoryPosition position = positionRepo.findByPositionID(positionID);
        Requirement requirement = requirementRepo.findByRequirementID(requirementID);
        Requirement_InventoryPosition requirementData = requir_invPosRepo.findByRequirementAndInventoryPosition(requirement, position);
        if (requirementData == null) {
            requirementData = new Requirement_InventoryPosition(requirement, position, value);
        } else {
            requirementData.setValue(value);
        }
        return requir_invPosRepo.saveAndFlush(requirementData);
    }
}
