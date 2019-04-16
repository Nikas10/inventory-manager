package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.exception.DeletionNotSupportedException;
import com.quartet.inventorydemo.model.InventoryItem;
import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.model.Role;
import com.quartet.inventorydemo.repository.RoleRepository;
import com.quartet.inventorydemo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service("RoleService")
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepo;

    @Override
    public List<Role> getAll() {
        return roleRepo.findAll();
    }

    @Override
    public Role getByRoleID(UUID roleID) {
        return roleRepo.findByRoleID(roleID);
    }

    @Override
    public Set<Role> getByRoleIDs(Set<UUID> uuidSet) {
        return roleRepo.findByRoleIDIn(uuidSet);
    }

    @Override
    public List<Role> getByRoleName(String roleName) {
        return roleRepo.findByName(roleName);
    }

    @Override
    public Role add(Role role) {
        role.setRoleID(UUID.randomUUID());
        return roleRepo.saveAndFlush(role);
    }

    @Override
    public Role update(Role role) {
        return roleRepo.saveAndFlush(role);
    }

    @Override
    public void remove(Role role) {
        Set<InventoryPosition> roleInventoryPositions = role.getRoleInventoryPositions();
        for (InventoryPosition roleInventoryPosition : roleInventoryPositions) {
            Set<InventoryItem> currentTypeItems = roleInventoryPosition.getCurrentTypeItems();
            if (!currentTypeItems.isEmpty()) {
                throw new DeletionNotSupportedException("can not delete role, while it is connected to positions with real items");
            }
        }
        roleRepo.delete(role);
    }


}
