package com.quartet.inventorydemo.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.*;

@Data
public class AddDeleteLinksForm {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final Set<UUID> addByIds = new HashSet<>();
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final Set<UUID> removeByIds = new HashSet<>();
    private List<String> add;       //for adding associations by id
    private List<String> remove;    //for removing associations by id

    public Set<UUID> getAddByIds() {
        addByIds.clear();
        addByIds.addAll(convertFromString(this.add));
        return addByIds;
    }

    public Set<UUID> getRemoveByIds() {
        removeByIds.clear();
        removeByIds.addAll(convertFromString(this.remove));
        return removeByIds;
    }

    private Set<UUID> convertFromString(Collection<String> stringUuids) {
        Set<UUID> uuids = new HashSet<>();
        for (String item : add) {
            try {
                UUID uuid = convertFromString(item);
                uuids.add(uuid);
            } catch (Exception e) {
                //TODO log or not catch
            }
        }
        return uuids;
    }

    private UUID convertFromString(String stringUuid) throws Exception {    //TODO custom converter exception
        try {
            return UUID.fromString(stringUuid);
        } catch (IllegalArgumentException e) {
            throw new Exception();
        }
    }
}
