package it.fulminazzo.giveevent;

import it.fulminazzo.events.EventPlugin;
import it.fulminazzo.giveevent.commands.GiveEventCommand;
import it.fulminazzo.giveevent.interfaces.IGiveEvent;
import it.fulminazzo.giveevent.managers.GiveEventManager;
import it.fulminazzo.giveevent.managers.GiveLocationsManager;
import it.fulminazzo.giveevent.managers.ItemsManager;
import it.fulminazzo.giveevent.objects.Enchant;
import it.fulminazzo.giveevent.objects.GiveLocation;
import it.fulminazzo.giveevent.objects.Item;
import it.fulminazzo.yamlparser.objects.configurations.FileConfiguration;
import it.fulminazzo.yamlparser.objects.yamlelements.CallableYAMLParser;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

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