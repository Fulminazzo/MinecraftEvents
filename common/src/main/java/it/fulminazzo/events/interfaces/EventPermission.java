package it.fulminazzo.events.interfaces;

import it.fulminazzo.events.EventPlugin;

public interface EventPermission {

    default String getPermission() {
        return String.format("%s.%s", EventPlugin.getPlugin().getName(), getRawPermission());
    }

    String getRawPermission();
}
