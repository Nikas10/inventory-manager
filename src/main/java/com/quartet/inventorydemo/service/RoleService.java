package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.model.Role;

import java.util.List;
import java.util.UUID;

public interface RoleService {
    List<Role> getAll();
    Role getByPositionID(UUID roleID);
    Role add(Role role);
    Role update(Role role);
}
