package com.quartet.inventorydemo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

@ApiModel
@Entity
@Data
@Table(name = "inventory_item", schema = "public")
public class InventoryItem implements Serializable {

    public InventoryItem() {
    }

    @Id
    @ApiModelProperty(hidden = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "inventory_item_id")
    private UUID inventoryItemID;

    @ApiModelProperty(hidden = true)
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private InventoryHolder inventoryHolder;

    @ApiModelProperty(hidden = true)
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private InventoryPosition inventoryPosition;

    @ApiModelProperty(position = 1, notes = "Email address")
    @NotNull
    @Column(name = "status")
    private String status;

    @ApiModelProperty(position = 2, notes = "Email address")
    @Column(name = "amount")
    private Integer amount;

}
