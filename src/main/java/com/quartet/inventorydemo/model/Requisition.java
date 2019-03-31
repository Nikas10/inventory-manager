package com.quartet.inventorydemo.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
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

    @Column (name = "creation_date")
    private Date creationDate;

    @Column (name = "due_date")
    private Date dueDate;

    @Column (name = "description")
    private String description;

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


}
