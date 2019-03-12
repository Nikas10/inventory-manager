package com.quartet.inventorydemo.model;

import lombok.Data;

import javax.persistence.*;
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
