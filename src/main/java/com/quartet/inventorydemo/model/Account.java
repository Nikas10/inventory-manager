package com.quartet.inventorydemo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@ApiModel
@Entity
@Data
@Table(name = "account", schema = "public")
public class Account implements Serializable {
    @Id
    @Column(name = "uid")
    @ApiModelProperty(hidden = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID uid;

    @ApiModelProperty(position = 1, required = true, notes = "Account login")
    @NotNull
    @Column (name = "login")
    private String login;

    @ApiModelProperty(position = 2, required = true, notes = "Account password")
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column (name = "pass")
    private String pass;

    @ApiModelProperty(hidden = true)
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column (name = "role")
    private String role = "user";

    @ApiModelProperty(position = 3, notes = "Email address")
    @NotNull
    @Column (name = "email")
    private String email;

    @ApiModelProperty(position = 4, notes = "First name")
    @Column(name = "first_name")
    private String firstName;

    @ApiModelProperty(position = 5, notes = "Middle name")
    @Column(name = "middle_name")
    private String middleName;

    @ApiModelProperty(position = 6, notes = "Family name")
    @Column(name = "last_name")
    private String lastName;

    @ApiModelProperty(hidden = true)
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "active")
    private Boolean active = true;


    @ApiModelProperty(hidden = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "employee_holder",
            joinColumns = @JoinColumn(name = "employeeID", referencedColumnName = "uid"),
            inverseJoinColumns = @JoinColumn(name = "holderID", referencedColumnName = "holderID")
    )
    private Set<InventoryHolder> currentHolders;

    @ApiModelProperty(hidden = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY)
    private Set<Requisition> employeeRequisitions;

    public Account() {
    }

    public Account(UUID uid, String name, String pass, String role, String email) {
        this.uid = uid;
        this.login = name;
        this.pass = pass;
        this.role = role;
        this.email = email;
    }

}