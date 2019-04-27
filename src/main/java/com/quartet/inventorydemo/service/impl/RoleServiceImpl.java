package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.model.Role;
import com.quartet.inventorydemo.repository.RoleRepository;
import com.quartet.inventorydemo.service.RoleService;
import com.quartet.inventorydemo.util.OnCreate;
import com.quartet.inventorydemo.util.OnUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service("RoleService")
@Validated
@org.springframework.transaction.annotation.Transactional
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(@Qualifier("RoleRepository") final RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    @Override
    public Role getByRoleID(@NotNull @Valid UUID roleID) {
        Optional<Role> byId = roleRepository.findById(roleID);
        byId.orElseThrow(ResourceNotFoundException::new);
        return byId.get();
    }

    @Override
    public Role getByRoleName(@NotBlank @Valid String roleName) {
        Optional<Role> byName = roleRepository.findByName(roleName);
        byName.orElseThrow(ResourceNotFoundException::new);
        return byName.get();
    }

    @Override
    public Set<Role> getByRoleIDs(@NotNull @Valid Set<UUID> uuidSet) {
        return roleRepository.findByIdIn(uuidSet);
    }

    @Validated(OnCreate.class)
    @Override
    public Role add(@NotNull @Valid Role role) {
        return roleRepository.saveAndFlush(role);
    }

    @Validated(OnUpdate.class)
    @Override
    public Role update(@NotNull @Valid Role role) {
        return roleRepository.saveAndFlush(role);
    }

    @Validated(OnUpdate.class)
    @Override
    public void remove(@NotNull @Valid Role role) {

        roleRepository.delete(role);
    }
}
