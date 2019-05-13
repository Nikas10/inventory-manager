package com.quartet.inventorydemo;

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
  private RequisitionService requisitionService;
  @Autowired
  private InventoryPositionService inventoryPositionService;
  @Autowired
  private InventoryItemService inventoryItemService;
  @Autowired
  private Bundle_InventoryPositionService bundle_inventoryPositionService;

  @Override
  public void afterPropertiesSet() throws Exception {
    Account admin = accountService.add(new Account("a", "a", "a", "a",
        passwordEncoder.encode("a"), "admin", "a@a"));
    Account staff = accountService.add(new Account("s", "s", "s", "s",
        passwordEncoder.encode("s"), "staff", "s@s"));
    Account user1 = accountService.add(new Account("u1", "u1", "u1", "u1",
        passwordEncoder.encode("u1"), "user", "u1@u1"));
    Account user2 = accountService.add(new Account("u2", "u2", "u2", "u2",
        passwordEncoder.encode("u2"), "user", "u2@u2"));

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

    InventoryPosition inventoryPosition1 = inventoryPositionService
        .add("inventory position name 1", "inventory position description 1", false);
    InventoryPosition inventoryPosition2 = inventoryPositionService
        .add("inventory position name 2", "inventory position description 2", false);
    InventoryPosition inventoryPosition3 = inventoryPositionService
        .add("inventory position name 3", "inventory position description 3", false);
    InventoryPosition inventoryPosition4 = inventoryPositionService
        .add("inventory position name 4", "inventory position description 4", false);
    InventoryPosition inventoryPosition5 = inventoryPositionService
        .add("inventory position name 5", "inventory position description 5", false);

    Requirement requirement1 = requirementService.add(new Requirement("requirement name 1"));
    Requirement requirement2 = requirementService.add(new Requirement("requirement name 2"));

    accountService.addHolders(user1.getLogin(),
        new HashSet<>(Arrays.asList(holder1.getId(), holder2.getId(), holder3.getId())));
    accountService.addHolders(user2.getLogin(),
        new HashSet<>(Arrays.asList(holder3.getId(), holder4.getId())));

    holderService.addRoles(holder1.getId(), new HashSet<>(Arrays.asList(role1.getId())));
    holderService
        .addRoles(holder2.getId(), new HashSet<>(Arrays.asList(role2.getId(), role3.getId())));
    holderService
        .addRoles(holder3.getId(), new HashSet<>(Arrays.asList(role3.getId(), role4.getId())));
    holderService
        .addRoles(holder4.getId(), new HashSet<>(Arrays.asList(role4.getId(), role1.getId())));

    roleService.addInventoryPositions(role1.getId(), new HashSet<>(Arrays.asList(inventoryPosition1.getId(),
        inventoryPosition2.getId(), inventoryPosition3.getId())));

    inventoryItemService.addToStorage(inventoryPosition1.getId(), 400);
    inventoryItemService.addToStorage(inventoryPosition2.getId(), 400);
    inventoryItemService.addToStorage(inventoryPosition3.getId(), 400);
    inventoryItemService.addToStorage(inventoryPosition4.getId(), 400);
    inventoryItemService.addToStorage(inventoryPosition5.getId(), 400);

    inventoryItemService.moveFromStorageToHolder(inventoryPosition1.getId(), holder1.getId(), 1);
    inventoryItemService.moveFromStorageToHolder(inventoryPosition2.getId(), holder2.getId(), 2);
    inventoryItemService.moveFromStorageToHolder(inventoryPosition3.getId(), holder3.getId(), 3);
    inventoryItemService.moveFromStorageToHolder(inventoryPosition4.getId(), holder4.getId(), 4);
    inventoryItemService.moveFromStorageToHolder(inventoryPosition5.getId(), holder5.getId(), 5);

    HashSet<UUID> adminHoldersIds = new HashSet<>();
    HashSet<UUID> staffHoldersIds = new HashSet<>();

    adminHoldersIds.add(holder1.getId());
    adminHoldersIds.add(holder2.getId());
    adminHoldersIds.add(holder3.getId());
    adminHoldersIds.add(holder4.getId());
    adminHoldersIds.add(holder5.getId());

    staffHoldersIds.add(holder1.getId());
    staffHoldersIds.add(holder2.getId());
    staffHoldersIds.add(holder3.getId());
    staffHoldersIds.add(holder5.getId());

    accountService.addHolders(admin.getLogin(), adminHoldersIds);
    accountService.addHolders(staff.getLogin(), staffHoldersIds);


    /*roleService.addInventoryPositions(role1.getId(),
        new HashSet<>(Arrays.asList(inventoryPosition1.getId())));
    roleService.addInventoryPositions(role2.getId(),
        new HashSet<>(Arrays.asList(inventoryPosition2.getId(), inventoryPosition3.getId(),
            inventoryPosition4.getId())));
    roleService.addInventoryPositions(role3.getId(),
        new HashSet<>(Arrays.asList(inventoryPosition3.getId(), inventoryPosition4.getId(),
            inventoryPosition5.getId())));*/

    /*Requisition adminReq = new Requisition(admin, admin, "REVIEW_NEEDED", new Date(), new Date(), "admin req 1.");
    Requisition staffReq = new Requisition(admin, admin, "REVIEW_NEEDED", new Date(), new Date(), "staff req 1.");
    Requisition userReq = new Requisition(admin, admin, "REVIEW_NEEDED", new Date(), new Date(), "user req 1.");
*/
    ArrayList<String> stringPositionIds = new ArrayList<>();
    stringPositionIds.add(inventoryPosition1.getId().toString());
    stringPositionIds.add(inventoryPosition2.getId().toString());
    stringPositionIds.add(inventoryPosition3.getId().toString());

    HashSet<UUID> roleIds = new HashSet<>();
    roleIds.add(role1.getId());
    roleIds.add(role2.getId());
    roleIds.add(role3.getId());

    //holderService.addRoles(holder1.getId(), roleIds);

    Date creationDate = new Date();
    Date dueDate = new Date(creationDate.getTime() + 1000000000);
    requisitionService.add(
        user1.getLogin(),
        creationDate,
        "user1 req 1.",
        dueDate,
        "REVIEW_NEEDED",
        holder1.getId(),
        stringPositionIds
        );
  }
}
