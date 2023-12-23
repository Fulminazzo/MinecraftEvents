package it.fulminazzo.enderdragonevent.enums;

import it.fulminazzo.events.interfaces.EventPermission;

public enum EDPermission implements EventPermission {
    START, STOP;

    private final String permission;

    EDPermission() {
        this.permission = name().toLowerCase();
    }

    @Override
    public String getRawPermission() {
        return permission;
    }
}
