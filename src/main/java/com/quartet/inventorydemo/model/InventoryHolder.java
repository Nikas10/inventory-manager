package com.quartet.inventorydemo.model;

import lombok.Data;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "inventoryHolder", schema = "public")
public class InventoryHolder {

    @Id
    @Column(name = "holderID")
    private UUID holderID;


    @Column (name = "name")
    private String name;

    @Column (name = "description")
    private String description;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "holder_role",
            joinColumns = @JoinColumn(name = "holderID", referencedColumnName = "holderID"),
            inverseJoinColumns = @JoinColumn(name = "roleID", referencedColumnName = "roleID")
    )
    private Set<Role> currentRoles;


    @ManyToMany(mappedBy = "currentHolders")
    private Set<Account> employeesWithHolder;

    @OneToMany(mappedBy = "inventoryHolder", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<InventoryItem> holdedItems;

    public InventoryHolder() {
    }

    public InventoryHolder(UUID holderID) {
        this.holderID = holderID;
    }

}
