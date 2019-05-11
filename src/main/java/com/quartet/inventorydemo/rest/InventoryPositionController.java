package com.quartet.inventorydemo.rest;

import com.quartet.inventorydemo.dto.InventoryPositionDTO;
import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.model.Bundle_InventoryPosition;
import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.model.RequirementValue;
import com.quartet.inventorydemo.service.Bundle_InventoryPositionService;
import com.quartet.inventorydemo.service.InventoryPositionService;
import com.quartet.inventorydemo.service.RequirementService;
import com.quartet.inventorydemo.service.RequirementValueService;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/position")
public class InventoryPositionController {

  private InventoryPositionService positionService;
  private RequirementValueService requirementValueService;
  private RequirementService requirementService;
  private Bundle_InventoryPositionService bundle_inventoryPositionService;

  @Autowired
  public InventoryPositionController(
      @Qualifier("InventoryPositionService") final InventoryPositionService positionService,
      @Qualifier("RequirementService") final RequirementService requirementService,
      @Qualifier("RequirementValueService") final RequirementValueService requirementValueService,
      @Qualifier("Bundle_InventoryPositionService")
      final Bundle_InventoryPositionService bundle_inventoryPositionService) {
    this.bundle_inventoryPositionService = bundle_inventoryPositionService;
    this.positionService = positionService;
    this.requirementService = requirementService;
    this.requirementValueService = requirementValueService;
  }

  @RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
  public ResponseEntity<?> get(@PathVariable("uuid") String stringUuid) {
    UUID id = UUID.fromString(stringUuid);
    Optional<InventoryPosition> optionalPosition = positionService.getByPositionID(id);
    optionalPosition.orElseThrow(
        () -> new ResourceNotFoundException("Position with id: " + id + " not found."));
    return new ResponseEntity<>(optionalPosition.get(), HttpStatus.OK);
  }

  @RequestMapping(value = "", method = RequestMethod.GET)
  public ResponseEntity<?> getAll() {
    return new ResponseEntity<>(positionService.getAll(), HttpStatus.OK);
  }

  @RequestMapping(value = "/isbundle/{uuid}", method = RequestMethod.GET)
  public ResponseEntity<?> isBundle(@PathVariable("uuid") String stringUuid) {
    UUID id = UUID.fromString(stringUuid);
    Optional<InventoryPosition> optionalPosition = positionService.getByPositionID(id);
    InventoryPosition result = optionalPosition.orElseThrow(
        () -> new ResourceNotFoundException("Position with id: " + id + " not found."));
    return new ResponseEntity<>(result.isBundle(), HttpStatus.OK);
  }

  @RequestMapping(value = "/setbundle/{uuid}/{value}", method = RequestMethod.POST)
  public ResponseEntity<?> setBundle(@PathVariable("uuid") String stringUuid,
      @PathVariable("value") Boolean value) {
    UUID id = UUID.fromString(stringUuid);
    InventoryPosition updatedPosition = positionService.setBundle(id, value);
    positionService.update(id, updatedPosition);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @RequestMapping(value = "", method = RequestMethod.POST)
  public ResponseEntity<?> create(@RequestBody InventoryPositionDTO positionDTO) {
    String description = positionDTO.getDescription();
    String name = positionDTO.getName();

    InventoryPosition newPosition = positionService.add(name, description);
    return new ResponseEntity<>(newPosition, HttpStatus.OK);
  }

  @RequestMapping(value = "{uuid}", method = RequestMethod.DELETE)
  public ResponseEntity<?> delete(@PathVariable("uuid") String stringUuid) {
    UUID id = UUID.fromString(stringUuid);
    Optional<InventoryPosition> byPositionID = positionService.getByPositionID(id);
    byPositionID.orElseThrow(
        () -> new ResourceNotFoundException("Position with id: " + id + " not found."));
    InventoryPosition removed = byPositionID.get();
    positionService.remove(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @RequestMapping(
      value = "{positionID}/requirement/{requirementID}/{value}",
      method = RequestMethod.POST)
  public ResponseEntity<?> createRequirementValue(
      @PathVariable("positionID") String stringPositionID,
      @PathVariable("requirementID") String stringRequirementID,
      @PathVariable("value") String requirementValue) {
    UUID positionID = UUID.fromString(stringPositionID);
    UUID requirementID = UUID.fromString(stringRequirementID);
    RequirementValue newProperty =
        requirementValueService.add(positionID, requirementID, requirementValue);

    return new ResponseEntity<>(newProperty, HttpStatus.OK);
  }

  @RequestMapping(
      value = "{positionID}/requirement/{requirementID}/{value}",
      method = RequestMethod.PATCH)
  public ResponseEntity<?> updateRequirementValue(
      @PathVariable("positionID") String stringPositionID,
      @PathVariable("requirementID") String stringRequirementID,
      @PathVariable("value") String value) {
    UUID positionID = UUID.fromString(stringPositionID);
    UUID requirementID = UUID.fromString(stringRequirementID);

    RequirementValue newProperty = requirementValueService.update(positionID, requirementID, value);

    return new ResponseEntity<>(newProperty, HttpStatus.OK);
  }

  @RequestMapping(value = "{positionID}/requirement/{requirementID}", method = RequestMethod.DELETE)
  public ResponseEntity<?> deleteRequirementValue(
      @PathVariable("positionID") String stringPositionID,
      @PathVariable("requirementID") String stringRequirementID) {
    UUID positionID = UUID.fromString(stringPositionID);
    UUID requirementID = UUID.fromString(stringRequirementID);

    requirementValueService.remove(positionID, requirementID);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @RequestMapping(value = "{positionID}/requirements", method = RequestMethod.GET)
  public ResponseEntity<?> getRequirements(@PathVariable("positionID") String stringPositionID) {
    UUID positionID = UUID.fromString(stringPositionID);
    return new ResponseEntity<>(
        requirementValueService.getRequirementsValues(positionID), HttpStatus.OK);
  }

  @RequestMapping(
      value = "bundles/{bundleId}/{partId}/{value}",
      method = RequestMethod.POST)
  public ResponseEntity<?> updateOrCreateBundle(
      @PathVariable("bundleId") String stringPositionID,
      @PathVariable("partId") String stringRequirementID,
      @PathVariable("value") Integer value) {
    UUID bundleId = UUID.fromString(stringPositionID);
    UUID partId = UUID.fromString(stringRequirementID);
    Optional<InventoryPosition> bundle = positionService.getByPositionID(bundleId);
    Optional<InventoryPosition> partOfBundle = positionService.getByPositionID(partId);
    if (!bundle.isPresent() || !partOfBundle.isPresent()) {
      throw new ResourceNotFoundException("Requested bundle or position are not found!");
    } else {
      Bundle_InventoryPosition bundlePart = bundle_inventoryPositionService.update(partOfBundle.get(), bundle.get(), value);
      return new ResponseEntity<>(bundlePart, HttpStatus.OK);
    }
  }

  @RequestMapping(
      value = "bundles/{bundleId}/{partId}",
      method = RequestMethod.GET)
  public ResponseEntity<?> getBundleValue(
      @PathVariable("bundleId") String stringPositionID,
      @PathVariable("partId") String stringRequirementID) {
    UUID bundleId = UUID.fromString(stringPositionID);
    UUID partId = UUID.fromString(stringRequirementID);
    Optional<InventoryPosition> bundle = positionService.getByPositionID(bundleId);
    Optional<InventoryPosition> partOfBundle = positionService.getByPositionID(partId);
    if (!bundle.isPresent() || !partOfBundle.isPresent()) {
      throw new ResourceNotFoundException("Requested bundle or position are not found!");
    } else {
      Integer bundlePartAmount = bundle_inventoryPositionService.getAmount(bundle.get(), partOfBundle.get());
      return new ResponseEntity<>(bundlePartAmount, HttpStatus.OK);
    }
  }

  @RequestMapping(
      value = "bundles/{bundleId}",
      method = RequestMethod.GET)
  public ResponseEntity<?> getBundleFirstLevelContents (
      @PathVariable("bundleId") String stringPositionID) {
    UUID bundleId = UUID.fromString(stringPositionID);
    List<InventoryPosition> result = bundle_inventoryPositionService.getBundleFirstLevelContents(bundleId);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

}
