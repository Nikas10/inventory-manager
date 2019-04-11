package com.quartet.inventorydemo.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.*;

@Data
public class CreateAndDeleteLinksForm {
    private List<String> add;       //for adding associations by id
    private List<String> remove;    //for removing associations by id

    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final Set<UUID> addIds = new HashSet<>();

    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final Set<UUID> removeIds = new HashSet<>();

    public Set<UUID> getAddIds() {
        addIds.clear();
        addIds.addAll(convertFromString(this.add));
        return addIds;
    }

    public Set<UUID> getRemoveIds() {
        removeIds.clear();
        removeIds.addAll(convertFromString(this.remove));
        return removeIds;
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
