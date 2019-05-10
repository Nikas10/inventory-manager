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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import org.hibernate.annotations.GenericGenerator;

@ApiModel(
    description =
        "This entity/form represents inventory position of any inventory item(like META information). It may be either bundle(position, that contains another position), nor simple position.")
@Entity(name = "InventoryPosition")
@Table(name = "quartet_inventory_position", schema = "public")
public class InventoryPosition extends History {

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

  @ApiModelProperty(position = 1, required = true, notes = "Inventory position name")
  @NotBlank(message = "Name must be not empty")
  @Column(name = "name", nullable = false, unique = true)
  private String name;

  @ApiModelProperty(position = 2, notes = "Inventory position description")
  @NotNull(message = "Description must be not null")
  @Column(name = "description", nullable = false)
  private String description;

  @ApiModelProperty(hidden = true)
  @ManyToMany(
      mappedBy = "inventoryPositions",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private Set<Role> roles;

  @ApiModelProperty(hidden = true)
  @OneToMany(mappedBy = "inventoryPosition", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private Set<Requisition_InventoryPosition> requisitionInventoryPositions;

  @ApiModelProperty(hidden = true)
  @OneToMany(mappedBy = "inventoryPosition", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private Set<InventoryItem> inventoryItems;

  @ApiModelProperty(hidden = true)
  @OneToMany(mappedBy = "bundlePosition", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private Set<Bundle_InventoryPosition> bundleInventoryPositions;

  @ApiModelProperty(hidden = true)
  @OneToMany(mappedBy = "inventoryPosition", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private Set<RequirementValue> requirementValues;

  private InventoryPosition() {
  }

  public InventoryPosition(
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

  public Set<Role> getRoles() {
    return roles;
  }

  public Set<Requisition_InventoryPosition> getRequisitionInventoryPositions() {
    return requisitionInventoryPositions;
  }

  public Set<InventoryItem> getInventoryItems() {
    return inventoryItems;
  }

  public Set<Bundle_InventoryPosition> getBundleInventoryPositions() {
    return bundleInventoryPositions;
  }

  public boolean isBundle() {

    if (getBundleInventoryPositions() != null) {
      return getBundleInventoryPositions().stream().anyMatch(x -> x.getBundlePosition().equals(this));
    } else return false;
  }
}
