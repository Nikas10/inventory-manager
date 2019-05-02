package com.quartet.inventorydemo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.quartet.inventorydemo.util.IdNotNull;
import com.quartet.inventorydemo.util.IdNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import org.hibernate.annotations.GenericGenerator;

@ApiModel(
    description =
        "This entity/form represents key of attribute value model for inventory position, like name of property")
@Entity(name = "Requirement")
@Table(name = "quartet_requirement", schema = "public")
public class Requirement extends History {

  @ApiModelProperty(hidden = true)
  @Null(groups = IdNull.class, message = "Trying to persist probably existing resource on create")
  @NotNull(groups = IdNotNull.class, message = "Resource id must be not null on update")
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "pg-uuid")
  @GenericGenerator(
      name = "pg-uuid",
      strategy = "uuid2",
      parameters =
      @org.hibernate.annotations.Parameter(
          name = "uuid_gen_strategy_class",
          value = "com.quartet.inventorydemo.model.id.PostgreSQLUUIDGenerationStrategy"))
  @Column(name = "id", nullable = false, updatable = false, unique = true)
  private UUID id;

  @ApiModelProperty(position = 1, required = true, notes = "Requirement name (name of property)")
  @NotBlank(message = "Name must be not empty")
  @Column(name = "name", nullable = false, unique = true)
  private String name;

  @ApiModelProperty(hidden = true)
  @OneToMany(mappedBy = "requirement", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private Set<RequirementValue> requirementValues;

  private Requirement() {
  }

  public Requirement(@NotBlank(message = "Name must be not empty") String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Requirement)) {
      return false;
    }
    Requirement that = (Requirement) o;
    return Objects.equals(id, that.id) && name.equals(that.name);
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
