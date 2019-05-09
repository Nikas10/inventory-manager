package com.quartet.inventorydemo;

import com.quartet.inventorydemo.model.Account;
import com.quartet.inventorydemo.model.Holder;
import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.model.Requirement;
import com.quartet.inventorydemo.model.Role;
import com.quartet.inventorydemo.service.AccountService;
import com.quartet.inventorydemo.service.Bundle_InventoryPositionService;
import com.quartet.inventorydemo.service.HolderService;
import com.quartet.inventorydemo.service.InventoryItemService;
import com.quartet.inventorydemo.service.InventoryPositionService;
import com.quartet.inventorydemo.service.RequirementService;
import com.quartet.inventorydemo.service.RoleService;
import java.util.Arrays;
import java.util.HashSet;
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
        .add("inventory position name 1", "inventory position description 1");
    InventoryPosition inventoryPosition2 = inventoryPositionService
        .add("inventory position name 2", "inventory position description 2");
    InventoryPosition inventoryPosition3 = inventoryPositionService
        .add("inventory position name 3", "inventory position description 3");
    InventoryPosition inventoryPosition4 = inventoryPositionService
        .add("inventory position name 4", "inventory position description 4");
    InventoryPosition inventoryPosition5 = inventoryPositionService
        .add("inventory position name 5", "inventory position description 5");

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

    roleService.addInventoryPositions(role1.getId(),
        new HashSet<>(Arrays.asList(inventoryPosition1.getId())));
    roleService.addInventoryPositions(role2.getId(),
        new HashSet<>(Arrays.asList(inventoryPosition2.getId(), inventoryPosition3.getId(),
            inventoryPosition4.getId())));
    roleService.addInventoryPositions(role3.getId(),
        new HashSet<>(Arrays.asList(inventoryPosition3.getId(), inventoryPosition4.getId(),
            inventoryPosition5.getId())));
  }
}
