package it.fulminazzo.enderdragonevent.events;

import it.fulminazzo.enderdragonevent.managers.EnderDragonManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

abstract class EnderDragonManagerEvent extends Event {
    @Getter
    protected final EnderDragonManager enderDragonManager;
    @Getter
    private static final HandlerList handlerList = new HandlerList();

    public EnderDragonManagerEvent(EnderDragonManager enderDragonManager) {
        super(!Bukkit.isPrimaryThread());
        this.enderDragonManager = enderDragonManager;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
