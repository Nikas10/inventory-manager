package com.quartet.inventorydemo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.quartet.inventorydemo.util.IdNotNull;
import com.quartet.inventorydemo.util.IdNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import org.hibernate.annotations.GenericGenerator;

@ApiModel(
    description =
        "This entity/form represents what inventory positions are allowed to be provided for holder")
@Entity(name = "Role")
@Table(name = "quartet_role", schema = "public")
public class Role extends History {

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

  @ApiModelProperty(position = 1, required = true, notes = "Role name")
  @NotBlank(message = "Name must be not empty")
  @Column(name = "name", nullable = false, unique = true)
  private String name;

  @ApiModelProperty(position = 2, notes = "Role description name")
  @NotNull(message = "Description must be not null")
  @Column(name = "description", nullable = false)
  private String description;

  @ApiModelProperty(hidden = true)
  @ManyToMany(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
  @JoinTable(
      name = "quartet_inventory_position__quartet_role",
      joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false),
      inverseJoinColumns =
      @JoinColumn(
          name = "inventory_position_id",
          referencedColumnName = "id",
          nullable = false))
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private Set<InventoryPosition> inventoryPositions;

  @ApiModelProperty(hidden = true)
  @ManyToMany(
      mappedBy = "roles",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private Set<Holder> holders;

  private Role() {
  }

  public Role(
      @NotBlank(message = "Name must be not empty") String name,
      @NotNull(message = "Description must be not null") String description) {
    this.name = name;
    this.description = description;
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

  public void setDescription(
      @NotNull(message = "Description must be not null") String description) {
    this.description = description;
  }

  public Set<InventoryPosition> getInventoryPositions() {
    return inventoryPositions;
  }

  public Set<Holder> getHolders() {
    return holders;
  }
}
