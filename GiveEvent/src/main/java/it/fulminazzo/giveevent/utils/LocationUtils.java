package it.fulminazzo.giveevent.utils;

import it.fulminazzo.giveevent.enums.Message;
import org.bukkit.Location;

public class LocationUtils {

    public static String formatMessage(String message, String locationName, Location location) {
        if (message == null) return null;
        if (locationName == null) locationName = "location";
        if (location == null || location.getWorld() == null) return message;
        return message
                .replace("%" + locationName + "%", Message.LOCATION_FORMAT.getMessage(false))
                .replace("%world%", location.getWorld().getName())
                .replace("%x%", String.valueOf(location.getBlockX()))
                .replace("%y%", String.valueOf(location.getBlockY()))
                .replace("%z%", String.valueOf(location.getBlockZ()));
    }
}