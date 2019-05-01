package com.quartet.inventorydemo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.quartet.inventorydemo.util.UUIDStringValidator;

import java.util.*;

public class CreateAndDeleteLinksForm {
    private List<String> add;       //for adding associations by id
    private List<String> remove;    //for removing associations by id

    @JsonIgnore
    private final Set<UUID> addIds = new HashSet<>();

    @JsonIgnore
    private final Set<UUID> removeIds = new HashSet<>();

    public List<String> getAdd() {
        return add;
    }

    public void setAdd(List<String> add) {
        this.add = add;
    }

    public List<String> getRemove() {
        return remove;
    }

    public void setRemove(List<String> remove) {
        this.remove = remove;
    }

    private static UUIDStringValidator validator = new UUIDStringValidator();

    /*converts current string list from add to UUID list*/
    public Set<UUID> convertAndGetAddIds() {
        addIds.clear();
        addIds.addAll(convertFromString(this.add));
        return addIds;
    }

    /*converts current string list from remove to UUID list*/
    public Set<UUID> convertAndGetRemoveIds() {
        removeIds.clear();
        removeIds.addAll(convertFromString(this.remove));
        return removeIds;
    }

    private Set<UUID> convertFromString(Collection<String> stringUuids) {
        Set<UUID> uuids = new HashSet<>();
        for (String item : stringUuids) {
            boolean isValid = validator.isValid(item, null);
            if (isValid) {
                UUID uuid = UUID.fromString(item);
                uuids.add(uuid);
            } else {
                //TODO log
            }
        }
        return uuids;
    }
}
