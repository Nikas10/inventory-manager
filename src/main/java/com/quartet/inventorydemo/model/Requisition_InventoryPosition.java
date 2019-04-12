package com.quartet.inventorydemo.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "requisition_inventory_position", schema = "public")
public class Requisition_InventoryPosition implements Serializable {

    public Requisition_InventoryPosition() {
    }

    @Id
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Requisition requisition;

    @Id
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private InventoryPosition inventoryPosition;


    @Column(name = "amount")
    private Integer amount;

    public void increaseAmount(Integer value) {
        this.amount += value;
    }
}
