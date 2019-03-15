package com.quartet.inventorydemo.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
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

    @ManyToMany(mappedBy = "roleInventoryPositions")
    private Set<Role> rolesWithItem;

    @OneToMany(mappedBy = "inventoryPosition", fetch = FetchType.EAGER)
    private Set<Request_InventoryPosition> allRequests;

    @OneToMany(mappedBy = "inventoryPosition", fetch = FetchType.EAGER)
    private Set<InventoryItem> currentTypeItems;

    public InventoryPosition() {
    }

    public InventoryPosition(UUID positionID, String name) {
        this.positionID = positionID;
        this.name = name;
    }

    public void setPositionID(UUID positionID) {
        this.positionID = positionID;
    }
}
