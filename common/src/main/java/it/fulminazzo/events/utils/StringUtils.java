package it.fulminazzo.events.utils;

import net.md_5.bungee.api.ChatColor;

public class StringUtils {

    public static String parseMessage(String message) {
        if (message == null) return null;
        try {
            Class.forName("net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer");
            message = net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection()
                    .serialize(net.kyori.adventure.text.minimessage.MiniMessage.miniMessage().deserialize(message));
        } catch (Exception ignored) {}
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}