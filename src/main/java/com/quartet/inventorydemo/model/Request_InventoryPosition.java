package com.quartet.inventorydemo.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "request_inventory_position", schema = "public")
public class Request_InventoryPosition implements Serializable {

    public Request_InventoryPosition() {
    }

    @Id
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Request request;

    @Id
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private InventoryPosition inventoryPosition;


    @Column(name = "amount")
    private Integer amount;

    public void increaseAmount(Integer value) {
        this.amount += value;
    }
}
