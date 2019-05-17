package com.quartet.inventorydemo;


import com.quartet.inventorydemo.dto.AmountDTO;
import com.quartet.inventorydemo.dto.RequirementValueUpdateDTO;
import com.quartet.inventorydemo.dto.RequisitionDTO;
import com.quartet.inventorydemo.dto.RequisitionInventoryPositionDTO;
import com.quartet.inventorydemo.model.Account;
import com.quartet.inventorydemo.model.Holder;
import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.model.Requirement;
import com.quartet.inventorydemo.model.Requisition;
import com.quartet.inventorydemo.model.Requisition_InventoryPosition;
import com.quartet.inventorydemo.model.Role;
import com.quartet.inventorydemo.service.AccountService;
import com.quartet.inventorydemo.service.Bundle_InventoryPositionService;
import com.quartet.inventorydemo.service.HolderService;
import com.quartet.inventorydemo.service.InventoryItemService;
import com.quartet.inventorydemo.service.InventoryPositionService;
import com.quartet.inventorydemo.service.RequirementService;
import com.quartet.inventorydemo.service.RequirementValueService;
import com.quartet.inventorydemo.service.RequisitionProcessService;
import com.quartet.inventorydemo.service.RequisitionService;
import com.quartet.inventorydemo.service.Requisition_InventoryPositionService;
import com.quartet.inventorydemo.service.RoleService;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SampleFiller implements InitializingBean {

  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private AccountService accountService;
  @Autowired
  private HolderService holderService;
  @Autowired
  private RoleService roleService;
  @Autowired
  private RequirementService requirementService;
  @Autowired
  private RequirementValueService requirementValueService;
  @Autowired
  private RequisitionProcessService requisitionProcessService;
  @Autowired
  private RequisitionService requisitionService;
  @Autowired
  private InventoryPositionService inventoryPositionService;
  @Autowired
  private InventoryItemService inventoryItemService;
  @Autowired
  private Bundle_InventoryPositionService bundle_inventoryPositionService;
  @Autowired
  private Requisition_InventoryPositionService requisition_inventoryPositionService;

  @Override
  public void afterPropertiesSet() throws Exception {
    Account admin =
        accountService.add(
            new Account("Dmitriy", "Andreevych", "Antsevych", "admin", passwordEncoder.encode("a"), "admin", "a@a"));
    Account staff =
        accountService.add(
            new Account("Dmitriy", "Sergeevych", "Myakishev", "staff", passwordEncoder.encode("s"), "staff", "s@s"));
    Account user1 =
        accountService.add(
            new Account("Kirill", "Sergeevych", "Danilyuk", "user1", passwordEncoder.encode("u1"), "user", "u1@u1"));
    Account user2 =
        accountService.add(
            new Account("Nikita", "Dmitrievych", "Abramenko", "user2", passwordEncoder.encode("u2"), "user", "u2@u2"));

    Holder holder1 = holderService.add("uses equipment for programming", "programmer");
    Holder holder2 = holderService.add("uses computer parts", "computer parts holder");
    Holder holder3 = holderService.add("holds nothing", "empty1");
    Holder holder4 = holderService.add("holds nothing", "empty2");
    Holder holder5 = holderService.add("holds nothing", "empty3");

    Role role1 = roleService.add(new Role("Holds any kind of tables.", "table holder"));
    Role role2 = roleService.add(new Role("Holds any kind of computers.", "computer holder"));
    Role role3 = roleService.add(new Role("Holds any kind of computer mouses.", "mouse holder"));
    Role role4 = roleService.add(new Role("Holds any kind of spoons.", "spoon holder"));
    Role role5 = roleService.add(new Role("Holds nothing.", "empty holder"));

    InventoryPosition inventoryPosition1 =
        inventoryPositionService.add(
            "table", "wooden table", false);
    InventoryPosition inventoryPosition2 =
        inventoryPositionService.add(
            "computer", "computer for work", false);
    InventoryPosition inventoryPosition3 =
        inventoryPositionService.add(
            "chair", "common chair for sitting", false);
    InventoryPosition inventoryPosition4 =
        inventoryPositionService.add(
            "gold spoon", "gold spoon for showing richness", false);
    InventoryPosition inventoryPosition5 =
        inventoryPositionService.add(
            "iron spoon", "iron spoon which has multifunctionality", false);

    Requirement requirement1 = requirementService.add(new Requirement("height"));
    Requirement requirement2 = requirementService.add(new Requirement("width"));

    requirementValueService.add(inventoryPosition1.getId(), requirement1.getId(),
        new RequirementValueUpdateDTO("3"));
    requirementValueService.add(inventoryPosition2.getId(), requirement1.getId(),
        new RequirementValueUpdateDTO("1"));
    requirementValueService.add(inventoryPosition2.getId(), requirement2.getId(),
        new RequirementValueUpdateDTO("2"));

    accountService.addHolder(user1.getLogin(), holder1.getId());
    accountService.addHolder(user1.getLogin(), holder2.getId());
    accountService.addHolder(user1.getLogin(), holder3.getId());

    accountService.addHolder(user2.getLogin(), holder3.getId());
    accountService.addHolder(user2.getLogin(), holder4.getId());

    holderService.addRole(holder1.getId(), role1.getId());
    holderService.addRole(holder1.getId(), role2.getId());

    holderService.addRole(holder2.getId(), role2.getId());
    holderService.addRole(holder2.getId(), role3.getId());

    holderService.addRole(holder3.getId(), role3.getId());
    holderService.addRole(holder3.getId(), role4.getId());

    holderService.addRole(holder4.getId(), role4.getId());
    holderService.addRole(holder4.getId(), role1.getId());

    roleService.addInventoryPosition(role1.getId(), inventoryPosition1.getId());
    roleService.addInventoryPosition(role1.getId(), inventoryPosition2.getId());
    roleService.addInventoryPosition(role1.getId(), inventoryPosition3.getId());
    roleService.addInventoryPosition(role2.getId(), inventoryPosition4.getId());

    inventoryItemService.addToStorage(inventoryPosition1.getId(), new AmountDTO(400));
    inventoryItemService.addToStorage(inventoryPosition2.getId(), new AmountDTO(400));
    inventoryItemService.addToStorage(inventoryPosition3.getId(), new AmountDTO(400));
    inventoryItemService.addToStorage(inventoryPosition4.getId(), new AmountDTO(400));
    inventoryItemService.addToStorage(inventoryPosition5.getId(), new AmountDTO(400));

    inventoryItemService
        .moveFromStorageToHolder(inventoryPosition1.getId(), holder1.getId(), new AmountDTO(1));
    inventoryItemService
        .moveFromStorageToHolder(inventoryPosition2.getId(), holder2.getId(), new AmountDTO(2));
    inventoryItemService
        .moveFromStorageToHolder(inventoryPosition3.getId(), holder3.getId(), new AmountDTO(3));
    inventoryItemService
        .moveFromStorageToHolder(inventoryPosition4.getId(), holder4.getId(), new AmountDTO(4));
    inventoryItemService
        .moveFromStorageToHolder(inventoryPosition5.getId(), holder5.getId(), new AmountDTO(5));

    accountService.addHolder(admin.getLogin(), holder1.getId());
    accountService.addHolder(admin.getLogin(), holder2.getId());
    accountService.addHolder(admin.getLogin(), holder3.getId());
    accountService.addHolder(admin.getLogin(), holder4.getId());
    accountService.addHolder(admin.getLogin(), holder5.getId());

    accountService.addHolder(staff.getLogin(), holder1.getId());
    accountService.addHolder(staff.getLogin(), holder2.getId());
    accountService.addHolder(staff.getLogin(), holder3.getId());
    accountService.addHolder(staff.getLogin(), holder4.getId());
    accountService.addHolder(staff.getLogin(), holder5.getId());

    ArrayList<InventoryPosition> stringPositionIds = new ArrayList<>();
    stringPositionIds.add(inventoryPosition1);
    stringPositionIds.add(inventoryPosition2);
    stringPositionIds.add(inventoryPosition3);

    HashSet<UUID> roleIds = new HashSet<>();
    roleIds.add(role1.getId());
    roleIds.add(role2.getId());
    roleIds.add(role3.getId());

    Date creationDate = new Date();
    Date dueDate = new Date(creationDate.getTime() + 1000000000);

    RequisitionDTO dto1 = new RequisitionDTO(null, "sample title 1",
        user1.getLogin(),
        null,
        "REVIEW_NEEDED",
        creationDate,
        dueDate,
        "user1 req 1.",
        holder1.getName(),
        holder1.getId().toString(),
        stringPositionIds.
            parallelStream()
            .map(e -> new RequisitionInventoryPositionDTO(e.getId().toString(), 1, e.getName(),
                e.getDescription()))
            .collect(Collectors.toList()));

    RequisitionDTO dto2 = new RequisitionDTO(null,"sample title 2",
        user2.getLogin(),
        null,
        "REVIEW_NEEDED",
        creationDate,
        dueDate,
        "user2 req 1.",
        holder1.getName(),
        holder1.getId().toString(),
        stringPositionIds.
            parallelStream()
            .map(e -> new RequisitionInventoryPositionDTO(e.getId().toString(), 1, e.getName(),
                e.getDescription()))
            .collect(Collectors.toList()));

    RequisitionDTO dto3 = new RequisitionDTO(null, "sample title 3",
        user2.getLogin(),
        null,
        "REVIEW_NEEDED",
        creationDate,
        dueDate,
        "user2 req 2.",
        holder1.getName(),
        holder1.getId().toString(),
        stringPositionIds.
            parallelStream()
            .map(e -> new RequisitionInventoryPositionDTO(e.getId().toString(), 1, e.getName(),
                e.getDescription()))
            .collect(Collectors.toList()));

    Requisition req1 =
        requisitionService.add(dto1);
    Requisition req2 =
        requisitionService.add(dto2);
    Requisition req3 =
        requisitionService.add(dto3);

    requisitionProcessService.create(req1);

    Requisition_InventoryPosition reqInvPos1 = new Requisition_InventoryPosition(inventoryPosition1,
        req1, 7);
    Requisition_InventoryPosition reqInvPos2 = new Requisition_InventoryPosition(inventoryPosition2,
        req1, 11);
    Requisition_InventoryPosition reqInvPos4 = new Requisition_InventoryPosition(inventoryPosition3,
        req1, 2);
    Requisition_InventoryPosition reqInvPos7 = new Requisition_InventoryPosition(inventoryPosition4,
        req1, 4);

    req1.getRequisitionInventoryPositions().add(reqInvPos1);
    req1.getRequisitionInventoryPositions().add(reqInvPos2);
    req1.getRequisitionInventoryPositions().add(reqInvPos4);
    req1.getRequisitionInventoryPositions().add(reqInvPos7);

    requisitionService.update(req1);

    requisitionProcessService.create(req2);

    Requisition_InventoryPosition reqInvPos3 = new Requisition_InventoryPosition(inventoryPosition2,
        req2, 9);
    Requisition_InventoryPosition reqInvPos5 = new Requisition_InventoryPosition(inventoryPosition3,
        req2, 11);
    Requisition_InventoryPosition reqInvPos6 = new Requisition_InventoryPosition(inventoryPosition1,
        req2, 3);

    req2.getRequisitionInventoryPositions().add(reqInvPos3);
    req2.getRequisitionInventoryPositions().add(reqInvPos5);
    req2.getRequisitionInventoryPositions().add(reqInvPos6);

    requisitionService.update(req2);

    requisitionProcessService.create(req3);
  }
}
