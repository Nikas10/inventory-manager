package com.quartet.inventorydemo.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "request", schema = "public")
public class Request {

    @Id
    @Column(name = "requestID")
    UUID requestID;

    @Column(name = "status")
    String status;

    @OneToMany(mappedBy = "request", fetch = FetchType.EAGER)
    private Set<Request_InventoryPosition> allPositions;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Employee creator;

    public Request() {
    }

    public Request(UUID requestID, String status) {
        this.requestID = requestID;
        this.status = status;
    }

    public void setRequestID(UUID requestID) {
        this.requestID = requestID;
    }
}
