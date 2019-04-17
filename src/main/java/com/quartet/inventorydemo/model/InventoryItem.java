package com.quartet.inventorydemo.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Data
@Table(name = "inventory_item", schema = "public")
public class InventoryItem implements Serializable {

    public InventoryItem() {
    }

    @Id
    @Column(name = "inventory_item_id")
    UUID inventoryItemID;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "holderID")
    InventoryHolder inventoryHolder;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "positionID")
    InventoryPosition inventoryPosition;

    @Column(name = "status")
    String status;

    @Column(name = "amount")
    Integer amount;

}
