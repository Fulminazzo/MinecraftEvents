package it.fulminazzo.events.utils;

import org.bukkit.Bukkit;

public class VersionsUtils {

    /**
     * Checks which version your plugin is being run on.
     * @param version the number of the version
     * @return true if the version matches or is higher than the one specified
     */
    public static boolean is1_(int version) {
        String serverVersion = Bukkit.getBukkitVersion().split("-")[0];
        serverVersion = serverVersion.substring(serverVersion.indexOf(".") + 1);
        serverVersion = serverVersion.substring(0, serverVersion.indexOf("."));
        return version <= Integer.parseInt(serverVersion);
    }
}