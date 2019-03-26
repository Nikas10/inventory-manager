package com.quartet.inventorydemo.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Entity
@Data
@Table(name = "account", schema = "public")
public class Account implements Serializable {
    @Id
    @Column(name = "uid")
    private UUID uid;

    @Column (name = "login")
    private String login;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column (name = "pass")
    private String pass;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column (name = "admin")
    private Boolean admin = false;

    @Column (name = "email")
    private String email;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "employee_holder",
            joinColumns = @JoinColumn(name = "employeeID", referencedColumnName = "uid"),
            inverseJoinColumns = @JoinColumn(name = "holderID", referencedColumnName = "holderID")
    )
    private Set<InventoryHolder> currentHolders;

    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Requisition> employeeRequisitions;

    public Account() {
    }

    public Account(UUID uid, String name, String pass, Boolean isadmin, String email) {
        this.uid = uid;
        this.login = name;
        this.pass = pass;
        this.admin = isadmin;
        this.email = email;
    }

}