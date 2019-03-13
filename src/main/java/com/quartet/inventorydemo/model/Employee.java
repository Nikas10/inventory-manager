package com.quartet.inventorydemo.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "employee", schema = "public")
public class Employee {
    @Id
    @Column(name = "employeeID")
    private UUID employeeID;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "employee_holder",
            joinColumns = @JoinColumn(name = "employeeID", referencedColumnName = "employeeID"),
            inverseJoinColumns = @JoinColumn(name = "holderID", referencedColumnName = "holderID")
    )
    private Set<InventoryHolder> currentHolders;



    @OneToMany(mappedBy = "creator", fetch = FetchType.EAGER)
    private Set<Request> employeeRequests;

    public Employee() {
    }

    public Employee(UUID employeeID) {
        this.employeeID = employeeID;
    }

    public void setHolderID(UUID holderID) {
        this.employeeID = employeeID;
    }
}
