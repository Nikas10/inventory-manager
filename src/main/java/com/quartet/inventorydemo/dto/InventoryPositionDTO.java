package com.quartet.inventorydemo.dto;


public class InventoryPositionDTO {
    private String name;
    private String description;

    InventoryPositionDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
