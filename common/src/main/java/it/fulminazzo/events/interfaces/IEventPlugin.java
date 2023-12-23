package it.fulminazzo.events.interfaces;

import it.fulminazzo.events.enums.EventLog;
import it.fulminazzo.events.utils.VersionsUtils;
import it.fulminazzo.yamlparser.objects.configurations.FileConfiguration;
import it.fulminazzo.yamlparser.utils.FileUtils;
import it.fulminazzo.yamlparser.utils.JarUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.logging.Logger;

public interface IEventPlugin {

    default void onEnable() {
        if (Arrays.stream(this.getClass().getInterfaces()).map(Class::getSimpleName)
                .noneMatch(f -> f.equalsIgnoreCase("I" + getName()))) {
            warning(EventLog.INVALID_CLASS, "%class%", this.getClass().getSimpleName(), "%expected%", "I" + getName());
            Bukkit.getPluginManager().disablePlugin((Plugin) this);
            return;
        }
        if (!VersionsUtils.is1_(getCompatibleVersion())) {
            Bukkit.getConsoleSender().sendMessage(EventLog.VERSION_NOT_COMPATIBLE.getMessage(
                    "%plugin%", getDisplayName(),
                    "%version%", String.valueOf(getCompatibleVersion())
            ));
            Bukkit.getPluginManager().disablePlugin((Plugin) this);
            return;
        }
        try {
            loadAll();
            Bukkit.getConsoleSender().sendMessage(EventLog.PLUGIN_ENABLED.getMessage(
                    "%plugin%", getDisplayName(),
                    "%version%", getDescription().getVersion(),
                    "%author%", getDescription().getAuthors().get(0)
            ));
        } catch (Exception e) {
            warning(EventLog.ERROR_DURING_ENABLE, "%error%", e.getMessage());
            Bukkit.getPluginManager().disablePlugin((Plugin) this);
        }
    }

    default void onDisable() {
        try {
            unloadAll();
            Bukkit.getConsoleSender().sendMessage(EventLog.PLUGIN_DISABLED.getMessage(
                    "%plugin%", getDisplayName(),
                    "%version%", getDescription().getVersion(),
                    "%author%", getDescription().getAuthors().get(0)
            ));
        } catch (Exception e) {
            warning(EventLog.ERROR_DURING_DISABLE, "%error%", e.getMessage());
        }
    }

    default void loadAll() throws Exception {
        unloadAll();
        loadConfigurations();
    }

    void loadConfigurations() throws IOException;

    default FileConfiguration loadConfiguration(String configName) throws IOException {
        if (!configName.endsWith(".yml")) configName += ".yml";
        File configFile = new File(getDataFolder(), configName);
        if (!configFile.exists()) {
            FileUtils.createNewFile(configFile);
            InputStream resource = JarUtils.getResource(configName);
            if (resource == null) return new FileConfiguration(configFile);
            FileUtils.writeToFile(configFile, resource);
        }
        return new FileConfiguration(configFile);
    }

    default void unloadAll() throws Exception {
        HandlerList.unregisterAll((Plugin) this);
    }

    FileConfiguration getConfiguration();

    FileConfiguration getLang();

    default void info(EventLog eventLog, String... strings) {
        if (eventLog != null) info(eventLog.getMessage(strings));
    }

    default void info(String message) {
        if (message != null) getLogger().info(message);
    }

    default void warning(EventLog eventLog, String... strings) {
        if (eventLog != null) warning(eventLog.getMessage(strings));
    }

    default void warning(String message) {
        if (message != null) getLogger().warning(message);
    }

    default int getCompatibleVersion() {
        return -1;
    }

    PluginCommand getCommand(String name);

    Logger getLogger();

    File getDataFolder();

    String getName();

    String getDisplayName();

    PluginDescriptionFile getDescription();
}