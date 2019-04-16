package com.quartet.inventorydemo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@ApiModel
@Entity
@Data
@Table(name = "requisition", schema = "public")
public class Requisition {

    @Id
    @ApiModelProperty(hidden = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "requestID")
    UUID requestID;

    @ApiModelProperty(hidden = true)
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "status")
    String status;

    @ApiModelProperty(position = 1, required = true, notes = "Creation date")
    @NotNull
    @Column(name = "creation_date")
    private Date creationDate;

    @ApiModelProperty(position = 2, required = true, notes = "Due date")
    @Column(name = "due_date")
    private Date dueDate;

    @ApiModelProperty(position = 3, notes = "Request description")
    @Column(name = "description")
    private String description;

    @ApiModelProperty(position = 4, required = true, notes = "Required positions")
    @OneToMany(mappedBy = "requisition", fetch = FetchType.LAZY)
    private Set<Requisition_InventoryPosition> allPositions;

    @ApiModelProperty(position = 5, required = true, notes = "Requester")
    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Account creator;

    public Requisition() {
    }

    public Requisition(UUID requestID, String status) {
        this.requestID = requestID;
        this.status = status;
    }
}
