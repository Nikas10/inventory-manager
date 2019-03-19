package com.quartet.inventorydemo.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "requisition", schema = "public")
public class Requisition {

    @Id
    @Column(name = "requestID")
    UUID requestID;

    @Column(name = "status")
    String status;

    @OneToMany(mappedBy = "requisition", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Requisition_InventoryPosition> allPositions;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Account creator;

    public Requisition() {
    }

    public Requisition(UUID requestID, String status) {
        this.requestID = requestID;
        this.status = status;
    }

    public void setRequestID(UUID requestID) {
        this.requestID = requestID;
    }
}
