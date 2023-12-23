package it.fulminazzo.minecraftevents;

import it.fulminazzo.enderdragonevent.services.EnderDragonService;
import it.fulminazzo.events.EventPlugin;
import it.fulminazzo.giveevent.managers.GiveEventManager;
import it.fulminazzo.giveevent.managers.GiveLocationsManager;
import it.fulminazzo.giveevent.managers.ItemsManager;
import it.fulminazzo.minecraftevents.interfaces.IMinecraftEvents;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MinecraftEvents extends EventPlugin implements IMinecraftEvents {
    private GiveEventManager giveEventManager;
    private ItemsManager itemsManager;
    private GiveLocationsManager giveLocationsManager;
    private EnderDragonService enderDragonService;

    public MinecraftEvents() {
        super();
        plugin = this;
    }

    @Override
    public void loadAll() throws Exception {
        super.loadAll();
        IMinecraftEvents.super.loadAll();
    }

    @Override
    public void unloadAll() throws Exception {
        super.unloadAll();
        IMinecraftEvents.super.unloadAll();
    }

    @Override
    public String getDisplayName() {
        return "&2Minecraft&6Events";
    }
}
