package it.fulminazzo.giveevent.enums;

import it.fulminazzo.events.interfaces.EventPermission;
import lombok.Getter;

@Getter
public enum GEPermission implements EventPermission {
    SET_GIVE_LOCATION, LIST_GIVE_LOCATIONS, REMOVE_GIVE_LOCATION,
    SET_ITEM, LIST_ITEMS, REMOVE_ITEM,
    START, STOP,
    ;

    private final String rawPermission;

    GEPermission() {
        this.rawPermission = name().toLowerCase().replace("_", "");
    }
}