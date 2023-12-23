package it.fulminazzo.giveevent;

import it.fulminazzo.events.EventPlugin;
import it.fulminazzo.giveevent.interfaces.IGiveEvent;
import it.fulminazzo.giveevent.managers.GiveEventManager;
import it.fulminazzo.giveevent.managers.GiveLocationsManager;
import it.fulminazzo.giveevent.managers.ItemsManager;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class GiveEvent extends EventPlugin implements IGiveEvent {
    private GiveEventManager giveEventManager;
    private ItemsManager itemsManager;
    private GiveLocationsManager giveLocationsManager;

    public GiveEvent() {
        super();
        plugin = this;
    }

    @Override
    public String getDisplayName() {
        return "&cGive&6Event";
    }
}