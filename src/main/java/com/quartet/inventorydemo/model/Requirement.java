package com.quartet.inventorydemo.model;

import com.quartet.inventorydemo.util.OnCreate;
import com.quartet.inventorydemo.util.OnUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@ApiModel(description = "This entity/form represents key of attribute value model for inventory position, like name of property")
@Entity(name = "Requirement")
@Table(name = "quartet_requirement", schema = "public")
public class Requirement extends History {

    @ApiModelProperty(hidden = true)
    @Null(groups = OnCreate.class, message = "Trying to persist probably existing resource on create")
    @NotNull(groups = OnUpdate.class, message = "Resource id must be not null on update")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "pg-uuid")
    @GenericGenerator(name = "pg-uuid", strategy = "uuid2",
            parameters = @org.hibernate.annotations.Parameter(
                    name = "uuid_gen_strategy_class",
                    value = "com.quartet.inventorydemo.repository.PostgreSQLUUIDGenerationStrategy"))
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private UUID id;

    @ApiModelProperty(position = 1, required = true, notes = "Requirement name (name of property)")
    @NotBlank(message = "Name must be not empty")
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ApiModelProperty(hidden = true)
    @OneToMany(mappedBy = "requirement", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<RequirementValue> requirementValues;

    private Requirement() {
    }

    public Requirement(@NotBlank(message = "Name must be not empty") String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Requirement)) return false;
        Requirement that = (Requirement) o;
        return Objects.equals(id, that.id) &&
                name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
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

    public Set<RequirementValue> getRequirementValues() {
        return requirementValues;
    }
}
