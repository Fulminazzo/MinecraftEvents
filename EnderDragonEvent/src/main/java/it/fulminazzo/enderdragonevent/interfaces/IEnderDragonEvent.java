package it.fulminazzo.enderdragonevent.interfaces;

import it.fulminazzo.enderdragonevent.commands.EnderDragonEventCommand;
import it.fulminazzo.enderdragonevent.listeners.EnderDragonListener;
import it.fulminazzo.enderdragonevent.listeners.RawEnderDragonListener;
import it.fulminazzo.enderdragonevent.services.EnderDragonService;
import it.fulminazzo.events.interfaces.IEventPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public interface IEnderDragonEvent extends IEventPlugin {
    int compatibleVersion = 16;

    default void loadAll() throws Exception {
        IEventPlugin.super.loadAll();
        setEnderDragonService(new EnderDragonService(this));
        Bukkit.getPluginManager().registerEvents(new RawEnderDragonListener(this), (Plugin) this);
        Bukkit.getPluginManager().registerEvents(new EnderDragonListener(this), (Plugin) this);
        getCommand("enderdragonevent").setExecutor(new EnderDragonEventCommand(this));
    }

    default void unloadAll() throws Exception {
        IEventPlugin.super.unloadAll();
        EnderDragonService enderDragonService = getEnderDragonService();
        if (enderDragonService != null) enderDragonService.stopAll();
    }

    @Override
    default int getCompatibleVersion() {
        return compatibleVersion;
    }

    void setEnderDragonService(EnderDragonService enderDragonService);

    EnderDragonService getEnderDragonService();
}
