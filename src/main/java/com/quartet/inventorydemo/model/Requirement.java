package com.quartet.inventorydemo.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "requirement", schema = "public")
public class Requirement {
    @Id
    @Column(name = "requirementID")
    private UUID requirementID;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "requirement", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Requirement_InventoryPosition> allPositions;

    public Requirement(String name) {

        this.name = name;
    }

    public void setRequirementID(UUID requirementID) {
        this.requirementID = requirementID;
    }
}
