package com.quartet.inventorydemo.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Data
@Table(name = "inventory_position_contents", schema = "public")
public class InventoryPositionContents implements Serializable {
    @Id
    @Column(name = "bundleID")
    private UUID bundleID;

    @Id
    @Column(name = "positionID")
    private UUID positionID;

    @Column(name = "amount")
    Integer amount;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private InventoryPosition bundle;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private InventoryPosition partOfInventoryPosition;

    public InventoryPositionContents() {
    }

    public InventoryPositionContents(UUID positionID, UUID bundleID, Integer amount) {
        this.positionID = positionID;
        this.bundleID = bundleID;
        this.amount = amount;
    }

}
