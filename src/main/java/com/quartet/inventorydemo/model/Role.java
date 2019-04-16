package com.quartet.inventorydemo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "role", schema = "public")
public class Role {

    @Id
    @ApiModelProperty(hidden = true)
    @Column(name = "roleID")
    private UUID roleID;

    @ApiModelProperty(position = 1, required = true, notes = "Role name")
    @NotNull
    @Column(name = "name")
    private String name;

    @ApiModelProperty(position = 2, notes = "Role description name")
    @Column(name = "description")
    private String description;

    @ApiModelProperty(hidden = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "role_inventoryPosition",
            joinColumns = @JoinColumn(name = "roleID", referencedColumnName = "roleID"),
            inverseJoinColumns = @JoinColumn(name = "positionID", referencedColumnName = "positionID")
    )
    private Set<InventoryPosition> roleInventoryPositions;

    @ApiModelProperty(hidden = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany(mappedBy = "currentRoles", cascade = CascadeType.ALL)
    private Set<InventoryHolder> allHolders;

    public Role() {
    }

    public Role(UUID positionID, String name) {
        this.roleID = positionID;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role role = (Role) o;
        return Objects.equals(getName(), role.getName()) &&
                Objects.equals(getDescription(), role.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription());
    }
}
