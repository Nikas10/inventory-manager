package com.quartet.inventorydemo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

@ApiModel
@Entity
@Data
@Table(name = "requirement", schema = "public")
public class Requirement {
    @Id
    @ApiModelProperty(hidden = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "requirementID")
    private UUID requirementID;

    @ApiModelProperty(position = 1, required = true, notes = "Requirement login")
    @NotNull
    @Column(name = "name")
    private String name;

    @ApiModelProperty(hidden = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "requirement", fetch = FetchType.LAZY)
    private Set<Requirement_InventoryPosition> allPositions;

    public Requirement(UUID requirementID, String name) {

        this.requirementID = requirementID;
        this.name = name;
    }

    public void setRequirementID(UUID requirementID) {
        this.requirementID = requirementID;
    }
}
