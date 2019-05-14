package com.quartet.inventorydemo.service.impl;

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
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
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

  @Autowired
  public RequisitionServiceImpl(
      @Qualifier("RequisitionRepository") final RequisitionRepository requisitionRepository,
      @Qualifier("AccountService") final AccountService accountService,
      @Qualifier("HolderService") final HolderService holderService,
      @Qualifier("InventoryPositionService") final InventoryPositionService inventoryPositionService) {
    this.requisitionRepository = requisitionRepository;
    this.accountService = accountService;
    this.holderService = holderService;
    this.inventoryPositionService = inventoryPositionService;
  }

  @Override
  public List<Requisition> getAll() {
    return requisitionRepository.findAll();
  }

  @Override
  public Optional<Requisition> getById(@NotNull @Valid UUID id) {
    return requisitionRepository.getById(id);
  }

  @Override
  public Requisition add(
      @NotNull @Valid String login,
      @NotNull @Valid Date creationDate,
      @NotNull @Valid String description,
      @NotNull @Valid Date dueDate,
      @NotNull @Valid String status,
      @NotNull @Valid UUID holderId,
      @NotEmpty @Valid Map<String, Integer> inventoryPositions) {

    Set<UUID> inventoryPositionUUIDs = new HashSet<>();
    for (String currentUUID: inventoryPositions.keySet()) {
      inventoryPositionUUIDs.add(UUID.fromString(currentUUID));
    }

    Optional<Holder> optionalHolder = holderService.getByHolderID(holderId);
    Holder accountHolder = optionalHolder.orElseThrow(
        () -> new ResourceNotFoundException("Holder with id: " + holderId + " does not exist."));

    Set<UUID> availableHolderPositionsIds = getHolderInventoryPositionsIds(accountHolder);

    checkPositionsExistance(inventoryPositionUUIDs);

    checkPositionsAvailability(availableHolderPositionsIds, inventoryPositionUUIDs);

    Optional<Account> optionalAccount = accountService.getByLogin(login);

    Requisition requisitionToAdd =
        new Requisition(
            optionalAccount.orElseThrow(
                () -> new ResourceNotFoundException("Account with login: " + login + " not found")),
            status,
            creationDate,
            dueDate,
            description,
            accountHolder);

    return requisitionRepository.saveAndFlush(requisitionToAdd);
  }

  @Override
  public Requisition update(@NotNull @Valid Requisition requisition) {
    return requisitionRepository.saveAndFlush(requisition);
  }

  @Override
  public void remove(@NotNull @Valid Requisition requisition) {
  }

  private Set<UUID> getHolderInventoryPositionsIds(Holder holder){
    Set<InventoryPosition> allPositions = new HashSet<>();

    for (Role currentRole: holder.getRoles()) {
      allPositions.addAll(currentRole.getInventoryPositions());
    }

    Set<UUID> positionsIds = new HashSet<>();
    for (InventoryPosition currentPosition : allPositions) {
      positionsIds.add(currentPosition.getId());
    }
    return positionsIds;
  }

  private void checkPositionsExistance(Set<UUID> positionsIDs) {
    for (UUID currentId :positionsIDs) {
      Optional<InventoryPosition> currentPosition = inventoryPositionService.getByPositionID(currentId);
      currentPosition.orElseThrow(() -> new ResourceNotFoundException("Positon with id: " + currentId + " not found."));
    }
  }

  private void checkPositionsAvailability(Set<UUID> availablePositions, Set<UUID> requestedPositions) {
    for (UUID currentId: requestedPositions) {
      if (!availablePositions.contains(currentId)) {
        throw new ResourceNotAvailableException("Inventory position with id:" + currentId + " is not available.");
      }
    }
  }
}
