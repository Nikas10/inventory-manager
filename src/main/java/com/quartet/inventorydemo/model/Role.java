package com.quartet.inventorydemo.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "role", schema = "public")
public class Role {

    @Id
    @Column(name = "roleID")
    private UUID roleID;

    @Column(name = "name")
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "role_inventoryPosition",
            joinColumns = @JoinColumn(name = "roleID", referencedColumnName = "roleID"),
            inverseJoinColumns = @JoinColumn(name = "positionID", referencedColumnName = "positionID")
    )
    private Set<InventoryPosition> roleInventoryPositions;

    @ManyToMany(mappedBy = "currentRoles")
    private Set<InventoryHolder> allHolders;

    public Role() {
    }

    public Role(UUID positionID, String name) {
        this.roleID = positionID;
        this.name = name;
    }

    public void setRoleID(UUID roleID) {
        this.roleID = roleID;
    }
}
