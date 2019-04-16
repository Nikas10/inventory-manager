package com.quartet.inventorydemo.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "inventoryPosition", schema = "public")
public class InventoryPosition {

    @Id
    @Column(name = "positionID")
    private UUID positionID;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "roleInventoryPositions")
    private Set<Role> rolesWithItem;

    @OneToMany(mappedBy = "inventoryPosition", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Requisition_InventoryPosition> allRequests;

    @OneToMany(mappedBy = "inventoryPosition", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<InventoryItem> currentTypeItems;

    @OneToMany(mappedBy = "bundle", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<InventoryPositionContents> bundleContent;

    @OneToMany(mappedBy = "partOfInventoryPosition",fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<InventoryPositionContents> partOfBundles;

    @OneToMany(mappedBy = "inventoryPosition", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Requisition_InventoryPosition> allPositions;

    public InventoryPosition() {
    }

    public InventoryPosition(UUID positionID, String name) {
        this.positionID = positionID;
        this.name = name;
    }
}
