package com.quartet.inventorydemo.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "requirement_inventory_position", schema = "public")
public class Requirement_InventoryPosition implements Serializable {

    @Column(name = "value")
    String value;
    @Id
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Requisition requirement;

    @Id
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private InventoryPosition inventoryPosition;

    public Requirement_InventoryPosition() {
    }
}
