package com.quartet.inventorydemo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "account", schema = "public")
public class Account implements Serializable {
    @Id
    @Column(name = "uid")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID uid;

    @Column (name = "login")
    private String login;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column (name = "pass")
    private String pass;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column (name = "role")
    private String role = "user";

    @Column (name = "email")
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "active")
    private Boolean active = true;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "employee_holder",
            joinColumns = @JoinColumn(name = "employeeID", referencedColumnName = "uid"),
            inverseJoinColumns = @JoinColumn(name = "holderID", referencedColumnName = "holderID")
    )
    private Set<InventoryHolder> currentHolders;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY, orphanRemoval = true)
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