package com.quartet.inventorydemo.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

//todo fix id's; add ApiModel and ApiModelProperty annotation
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

    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private InventoryPosition bundle;

    @NotNull
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
