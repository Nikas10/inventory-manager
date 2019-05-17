package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.dto.RequisitionDTO;
import com.quartet.inventorydemo.dto.RequisitionInventoryPositionDTO;
import com.quartet.inventorydemo.exception.ResourceNotAvailableException;
import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.model.Account;
import com.quartet.inventorydemo.model.Holder;
import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.model.Requisition;
import com.quartet.inventorydemo.model.Role;
import com.quartet.inventorydemo.repository.RequisitionRepository;
import com.quartet.inventorydemo.service.AccountService;
import com.quartet.inventorydemo.service.HolderService;
import com.quartet.inventorydemo.service.InventoryPositionService;
import com.quartet.inventorydemo.service.RequisitionService;
import com.quartet.inventorydemo.service.Requisition_InventoryPositionService;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service("RequisitionService")
@Validated
@org.springframework.transaction.annotation.Transactional
public class RequisitionServiceImpl implements RequisitionService {

  private final RequisitionRepository requisitionRepository;
  private final AccountService accountService;
  private final HolderService holderService;
  private final InventoryPositionService inventoryPositionService;
  private final Requisition_InventoryPositionService requisition_InventoryPositionService;

  @Autowired
  public RequisitionServiceImpl(
      @Qualifier("RequisitionRepository") final RequisitionRepository requisitionRepository,
      @Qualifier("AccountService") final AccountService accountService,
      @Qualifier("HolderService") final HolderService holderService,
      @Qualifier("InventoryPositionService") final InventoryPositionService inventoryPositionService,
      @Qualifier("Requisition_InventoryPositionService") final Requisition_InventoryPositionService requisition_InventoryPositionService) {
    this.requisitionRepository = requisitionRepository;
    this.accountService = accountService;
    this.holderService = holderService;
    this.inventoryPositionService = inventoryPositionService;
    this.requisition_InventoryPositionService = requisition_InventoryPositionService;
  }

  @Override
  public List<Requisition> getAll() {
    return requisitionRepository.findAll();
  }

  @Override
  public Requisition getById(@NotNull @Valid UUID id) {
    Optional<Requisition> optionalRequisition = requisitionRepository.getById(id);
    Requisition requisition = optionalRequisition
        .orElseThrow(() -> new ResourceNotFoundException("Requisition with id "
            + id
            + " is not found!"));
    return requisition;
  }

  @Override
  public Requisition add(
      @NotNull @Valid RequisitionDTO requisitionDTO) {
    Holder accountHolder = holderService
        .getByHolderID(UUID.fromString(requisitionDTO.getHolderUUID()))
        .orElseThrow(
            () -> new ResourceNotFoundException("Holder with id: "
                + requisitionDTO.getHolderUUID() + " does not exist."));
    Account account = accountService.getByLogin(requisitionDTO.getLogin())
        .orElseThrow(
            () -> new ResourceNotFoundException("Account with login: "
                + requisitionDTO.getLogin() + " does not exist."));
    Requisition requisitionToAdd = requisitionRepository.saveAndFlush(new Requisition(
        account,
        requisitionDTO.getStatus(),
        requisitionDTO.getCreationDate(),
        requisitionDTO.getDueDate(),
        requisitionDTO.getTitle(),
        requisitionDTO.getDescription(),
        accountHolder));
    List<RequisitionInventoryPositionDTO> positionsToPatch = requisitionDTO.getInventoryPositions();
    requisitionToAdd.setRequisitionInventoryPositions(
        new HashSet<>());
    requisitionToAdd.getRequisitionInventoryPositions()
        .addAll(requisition_InventoryPositionService
            .addAllByInventory(requisitionToAdd, positionsToPatch));
    return requisitionToAdd;
  }

  @Override
  public Requisition update(@NotNull @Valid Requisition requisition) {
    return requisitionRepository.saveAndFlush(requisition);
  }

  @Override
  public void remove(@NotNull @Valid Requisition requisition) {
  }

  private Set<UUID> getHolderInventoryPositionsIds(Holder holder) {
    Set<InventoryPosition> allPositions = new HashSet<>();

    for (Role currentRole : holder.getRoles()) {
      allPositions.addAll(currentRole.getInventoryPositions());
    }

    Set<UUID> positionsIds = new HashSet<>();
    for (InventoryPosition currentPosition : allPositions) {
      positionsIds.add(currentPosition.getId());
    }
    return positionsIds;
  }

  private void checkPositionsExistance(Set<UUID> positionsIDs) {
    for (UUID currentId : positionsIDs) {
      Optional<InventoryPosition> currentPosition = inventoryPositionService
          .getByPositionID(currentId);
      currentPosition.orElseThrow(
          () -> new ResourceNotFoundException("Positon with id: " + currentId + " not found."));
    }
  }

  private void checkPositionsAvailability(Set<UUID> availablePositions,
      Set<UUID> requestedPositions) {
    for (UUID currentId : requestedPositions) {
      if (!availablePositions.contains(currentId)) {
        throw new ResourceNotAvailableException(
            "Inventory position with id:" + currentId + " is not available.");
      }
    }
  }
}
