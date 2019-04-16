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
@Table(name = "inventoryHolder", schema = "public")
public class InventoryHolder {

    @Id
    @ApiModelProperty(hidden = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "holderID")
    private UUID holderID;

    @ApiModelProperty(position = 1, required = true, notes = "Holder name")
    @Column(name = "name")
    @NotNull
    private String name;

    @ApiModelProperty(position = 2, notes = "Holder description")
    @Column(name = "description")
    private String description;

    @ApiModelProperty(hidden = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "holder_role",
            joinColumns = @JoinColumn(name = "holderID", referencedColumnName = "holderID"),
            inverseJoinColumns = @JoinColumn(name = "roleID", referencedColumnName = "roleID")
    )
    private Set<Role> currentRoles;

    @ApiModelProperty(hidden = true)
    @ManyToMany(mappedBy = "currentHolders")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<Account> employeesWithHolder;

    @ApiModelProperty(hidden = true)
    @OneToMany(mappedBy = "inventoryHolder", fetch = FetchType.LAZY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<InventoryItem> holdedItems;

    public InventoryHolder() {
    }

    public InventoryHolder(UUID holderID) {
        this.holderID = holderID;
    }
}
