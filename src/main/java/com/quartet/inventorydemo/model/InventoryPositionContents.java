package com.quartet.inventorydemo.model;

import com.quartet.inventorydemo.model.id.InventoryPositionContentsID;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity @IdClass(InventoryPositionContentsID.class)
@Data
@Table(name = "inventory_position_contents", schema = "public")
public class InventoryPositionContents implements Serializable {

    @Column(name = "amount")
    Integer amount;

    @Id
    @JoinColumn(name = "BundleID")
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private InventoryPosition bundle;

    @Id
    @JoinColumn(name = "PositionID")
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private InventoryPosition partOfInventoryPosition;

    public InventoryPositionContents() {
    }

    public InventoryPositionContents(Integer amount, InventoryPosition bundle, InventoryPosition partOfInventoryPosition) {
        this.amount = amount;
        this.bundle = bundle;
        this.partOfInventoryPosition = partOfInventoryPosition;
    }
}
