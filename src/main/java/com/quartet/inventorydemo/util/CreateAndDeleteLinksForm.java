package com.quartet.inventorydemo.util;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    public Set<UUID> getAddIds() {
        if (this.add != null) {
            addIds.clear();
            addIds.addAll(convertFromString(this.add));
        }
        return addIds;
    }

    public Set<UUID> getRemoveIds() {
        if (this.remove != null) {
            removeIds.clear();
            removeIds.addAll(convertFromString(this.remove));
        }
        return removeIds;
    }

    private Set<UUID> convertFromString(Collection<String> stringUuids) {
        Set<UUID> uuids = new HashSet<>();
        for (String item : stringUuids) {
            try {
                UUID uuid = UUID.fromString(item);
                uuids.add(uuid);
            } catch (IllegalArgumentException e) {
                //TODO log
            }
        }
        return uuids;
    }
}
