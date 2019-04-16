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
@Table(name = "inventoryPosition", schema = "public")
public class InventoryPosition {

    @Id
    @ApiModelProperty(hidden = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "positionID")
    private UUID positionID;

    @ApiModelProperty(position = 1, required = true, notes = "Inventory name")
    @NotNull
    @Column(name = "name")
    private String name;

    @ApiModelProperty(position = 2, notes = "Inventory description")
    @Column(name = "description")
    private String description;

    @ApiModelProperty(hidden = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany(mappedBy = "roleInventoryPositions")
    private Set<Role> rolesWithItem;

    @ApiModelProperty(hidden = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "inventoryPosition", fetch = FetchType.LAZY)
    private Set<Requisition_InventoryPosition> allRequests;

    @ApiModelProperty(hidden = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "inventoryPosition", fetch = FetchType.LAZY)
    private Set<InventoryItem> currentTypeItems;

    @ApiModelProperty(hidden = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "bundleID")
    private Set<InventoryPositionContents> bundleContent;

    @ApiModelProperty(hidden = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "positionID")
    private Set<InventoryPositionContents> partOfBundles;

    @ApiModelProperty(hidden = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "inventoryPosition", fetch = FetchType.LAZY)
    private Set<Requisition_InventoryPosition> allPositions;

    public InventoryPosition() {
    }

    public InventoryPosition(UUID positionID, String name) {
        this.positionID = positionID;
        this.name = name;
    }
}
