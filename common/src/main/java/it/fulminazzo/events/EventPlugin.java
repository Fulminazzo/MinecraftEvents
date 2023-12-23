package it.fulminazzo.events;

import it.fulminazzo.events.interfaces.IEventPlugin;
import it.fulminazzo.yamlparser.objects.configurations.FileConfiguration;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

@Getter
public abstract class EventPlugin extends JavaPlugin implements IEventPlugin {
    @Getter
    protected static IEventPlugin plugin;
    protected FileConfiguration configuration;
    protected FileConfiguration lang;

    @Override
    public void onEnable() {
        IEventPlugin.super.onEnable();
    }

    @Override
    public void onDisable() {
        IEventPlugin.super.onDisable();
    }

    @Override
    public void loadConfigurations() throws IOException {
        configuration = loadConfiguration("config");
        lang = loadConfiguration("messages");
    }
}