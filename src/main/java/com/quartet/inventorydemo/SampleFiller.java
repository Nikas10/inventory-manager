package com.quartet.inventorydemo;

import com.quartet.inventorydemo.dto.AddInventoryItemToStorageDTO;
import com.quartet.inventorydemo.dto.RequirementValueUpdateDTO;
import com.quartet.inventorydemo.model.Account;
import com.quartet.inventorydemo.model.Holder;
import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.model.Requirement;
import com.quartet.inventorydemo.model.Requisition;
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
import com.quartet.inventorydemo.service.RoleService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;
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

  @Override
  public void afterPropertiesSet() throws Exception {
    Account admin =
        accountService.add(
            new Account("a", "a", "a", "a", passwordEncoder.encode("a"), "admin", "a@a"));
    Account staff =
        accountService.add(
            new Account("s", "s", "s", "s", passwordEncoder.encode("s"), "staff", "s@s"));
    Account user1 =
        accountService.add(
            new Account("u1", "u1", "u1", "u1", passwordEncoder.encode("u1"), "user", "u1@u1"));
    Account user2 =
        accountService.add(
            new Account("u2", "u2", "u2", "u2", passwordEncoder.encode("u2"), "user", "u2@u2"));

    Holder holder1 = holderService.add("holder description 1", "holder name 1");
    Holder holder2 = holderService.add("holder description 2", "holder name 2");
    Holder holder3 = holderService.add("holder description 3", "holder name 3");
    Holder holder4 = holderService.add("holder description 4", "holder name 4");
    Holder holder5 = holderService.add("holder description 5", "holder name 5");

    Role role1 = roleService.add(new Role("role name 1", "role description 1"));
    Role role2 = roleService.add(new Role("role name 2", "role description 2"));
    Role role3 = roleService.add(new Role("role name 3", "role description 3"));
    Role role4 = roleService.add(new Role("role name 4", "role description 4"));
    Role role5 = roleService.add(new Role("role name 5", "role description 5"));

    InventoryPosition inventoryPosition1 =
        inventoryPositionService.add(
            "inventory position name 1", "inventory position description 1", false);
    InventoryPosition inventoryPosition2 =
        inventoryPositionService.add(
            "inventory position name 2", "inventory position description 2", false);
    InventoryPosition inventoryPosition3 =
        inventoryPositionService.add(
            "inventory position name 3", "inventory position description 3", false);
    InventoryPosition inventoryPosition4 =
        inventoryPositionService.add(
            "inventory position name 4", "inventory position description 4", false);
    InventoryPosition inventoryPosition5 =
        inventoryPositionService.add(
            "inventory position name 5", "inventory position description 5", false);

    Requirement requirement1 = requirementService.add(new Requirement("requirement name 1"));
    Requirement requirement2 = requirementService.add(new Requirement("requirement name 2"));

    requirementValueService.add(inventoryPosition1.getId(), requirement1.getId(), new RequirementValueUpdateDTO(
        "requirement value 1"));
    requirementValueService.add(inventoryPosition2.getId(), requirement1.getId(), new RequirementValueUpdateDTO("requirement value 1 (2)"));
    requirementValueService.add(inventoryPosition2.getId(), requirement2.getId(), new RequirementValueUpdateDTO("requirement value 1"));

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

    inventoryItemService.addToStorage(inventoryPosition1.getId(), new AddInventoryItemToStorageDTO(400));
    inventoryItemService.addToStorage(inventoryPosition2.getId(), new AddInventoryItemToStorageDTO(400));
    inventoryItemService.addToStorage(inventoryPosition3.getId(), new AddInventoryItemToStorageDTO(400));
    inventoryItemService.addToStorage(inventoryPosition4.getId(), new AddInventoryItemToStorageDTO(400));
    inventoryItemService.addToStorage(inventoryPosition5.getId(), new AddInventoryItemToStorageDTO(400));

    inventoryItemService.moveFromStorageToHolder(inventoryPosition1.getId(), holder1.getId(), new AddInventoryItemToStorageDTO(1));
    inventoryItemService.moveFromStorageToHolder(inventoryPosition2.getId(), holder2.getId(), new AddInventoryItemToStorageDTO(2));
    inventoryItemService.moveFromStorageToHolder(inventoryPosition3.getId(), holder3.getId(), new AddInventoryItemToStorageDTO(3));
    inventoryItemService.moveFromStorageToHolder(inventoryPosition4.getId(), holder4.getId(), new AddInventoryItemToStorageDTO(4));
    inventoryItemService.moveFromStorageToHolder(inventoryPosition5.getId(), holder5.getId(), new AddInventoryItemToStorageDTO(5));

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

    ArrayList<String> stringPositionIds = new ArrayList<>();
    stringPositionIds.add(inventoryPosition1.getId().toString());
    stringPositionIds.add(inventoryPosition2.getId().toString());
    stringPositionIds.add(inventoryPosition3.getId().toString());

    HashSet<UUID> roleIds = new HashSet<>();
    roleIds.add(role1.getId());
    roleIds.add(role2.getId());
    roleIds.add(role3.getId());

    Date creationDate = new Date();
    Date dueDate = new Date(creationDate.getTime() + 1000000000);

    Requisition req1 =
        requisitionService.add(
            user1.getLogin(),
            creationDate,
            "user1 req 1.",
            dueDate,
            "REVIEW_NEEDED",
            holder1.getId(),
            stringPositionIds);
    Requisition req2 =
        requisitionService.add(
            user2.getLogin(),
            creationDate,
            "user2 req 1.",
            dueDate,
            "REJECTED",
            holder1.getId(),
            stringPositionIds);

    requisitionProcessService.create(req1);
    requisitionProcessService.create(req2);
  }
}
