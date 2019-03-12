package com.quartet.inventorydemo.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "inventoryHolder", schema = "public")
public class InventoryHolder {

    @Id
    @Column(name = "holderID")
    private UUID holderID;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "holder_role",
            joinColumns = @JoinColumn(name = "holderID", referencedColumnName = "holderID"),
            inverseJoinColumns = @JoinColumn(name = "roleID", referencedColumnName = "roleID")
    )
    private Set<Role> currentRoles;


    @ManyToMany(mappedBy = "currentHolders")
    private Set<Employee> employeesWithHolder;

    public InventoryHolder() {
    }

    public InventoryHolder(UUID holderID) {
        this.holderID = holderID;
    }

    public void setHolderID(UUID holderID) {
        this.holderID = holderID;
    }
}
