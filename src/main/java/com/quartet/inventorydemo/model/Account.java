package com.quartet.inventorydemo.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;
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

    @Column (name = "pass")
    private String pass;

    @Column (name = "admin")
    private Boolean admin;

    @Column (name = "email")
    private String email;

    @Column (name = "is_active")
    private Boolean isActive;

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