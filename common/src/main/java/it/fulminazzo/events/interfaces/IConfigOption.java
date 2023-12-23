package it.fulminazzo.events.interfaces;

import it.fulminazzo.events.EventPlugin;
import it.fulminazzo.yamlparser.objects.configurations.ConfigurationSection;
import it.fulminazzo.yamlparser.objects.configurations.FileConfiguration;

import java.util.List;

public interface IConfigOption {

    default Integer getInteger() {
        return getConfiguration().getInteger(getPath());
    }

    default Boolean getBoolean() {
        return getConfiguration().getBoolean(getPath());
    }

    default String getString() {
        return getConfiguration().getString(getPath());
    }

    default List<String> getStringList() {
        return getConfiguration().getStringList(getPath());
    }

    default ConfigurationSection getSection() {
        return getConfiguration().getConfigurationSection(getPath());
    }

    String getPath();

    default FileConfiguration getConfiguration() {
        return EventPlugin.getPlugin().getConfiguration();
    }
}
