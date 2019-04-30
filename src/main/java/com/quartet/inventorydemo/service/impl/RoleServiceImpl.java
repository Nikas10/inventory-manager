package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.exception.ResourceAlreadyExistsException;
import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.model.Role;
import com.quartet.inventorydemo.repository.RoleRepository;
import com.quartet.inventorydemo.service.RoleService;
import com.quartet.inventorydemo.util.IdNull;
import com.quartet.inventorydemo.util.IdNotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
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

    @Validated(IdNull.class)
    @Override
    public Role add(@NotNull @Valid Role role) {
        ExampleMatcher nameIgnoreSensitivityMatcher = ExampleMatcher.matchingAny()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.ignoreCase())
                .withIgnorePaths("id", "description", "inventoryPositions", "holders");
        Example<Role> roleExample = Example.of(role, nameIgnoreSensitivityMatcher);
        Optional<Role> alreadyExists = roleRepository.findOne(roleExample);

        alreadyExists.ifPresent(e -> {
            throw new ResourceAlreadyExistsException("role with same name already exists");
        });

        return alreadyExists.orElseGet(() -> {
            Role newRole = new Role(role.getName(), role.getDescription());
            return roleRepository.saveAndFlush(newRole);
        });
    }

    @Validated(IdNotNull.class)
    @Override
    public Role update(@NotNull @Valid Role role) {
        return roleRepository.saveAndFlush(role);
    }

    @Validated(IdNotNull.class)
    @Override
    public void remove(@NotNull @Valid Role role) {

        roleRepository.delete(role);
    }
}
