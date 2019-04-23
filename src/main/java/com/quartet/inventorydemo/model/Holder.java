package com.quartet.inventorydemo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@ApiModel(description = "This entity/form represents profile that holds some inventory objects. May be storehouse profile or any user profile")
@Entity(name = "Holder")
@Table(name = "quartet_holder", schema = "public")
public class Holder {

    @ApiModelProperty(hidden = true)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "pg-uuid")
    @GenericGenerator(name = "pg-uuid", strategy = "uuid2",
            parameters = @org.hibernate.annotations.Parameter(
                    name = "uuid_gen_strategy_class",
                    value = "com.quartet.inventorydemo.repository.PostgreSQLUUIDGenerationStrategy"))
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private UUID id;

    @ApiModelProperty(position = 1, required = true, notes = "Holder name")
    @NotBlank(message = "Name must be not empty")
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ApiModelProperty(position = 2, notes = "Holder description")
    @NotNull(message = "Description must be not null")
    @Column(name = "description", nullable = false)
    private String description;

    @ApiModelProperty(hidden = true)
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "quartet_holder__quartet_role",
            joinColumns = @JoinColumn(name = "holder_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    )
    private Set<Role> roles;

    @ApiModelProperty(hidden = true)
    @ManyToMany(mappedBy = "holders", fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<Account> accounts;

    @ApiModelProperty(hidden = true)
    @OneToMany(mappedBy = "holder", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<InventoryItem> inventoryItems;

    private Holder() {
    }

    public Holder(@NotBlank(message = "Name must be not empty") String name,
                  @NotNull(message = "Description must be not null") String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Holder)) return false;
        Holder holder = (Holder) o;
        return Objects.equals(id, holder.id) &&
                name.equals(holder.name) &&
                description.equals(holder.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Name must be not empty") String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(@NotNull(message = "Description must be not null") String description) {
        this.description = description;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public Set<InventoryItem> getInventoryItems() {
        return inventoryItems;
    }
}
