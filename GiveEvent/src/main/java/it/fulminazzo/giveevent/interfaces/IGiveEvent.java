package it.fulminazzo.giveevent.interfaces;

import it.fulminazzo.events.interfaces.IEventPlugin;
import it.fulminazzo.giveevent.commands.GiveEventCommand;
import it.fulminazzo.giveevent.managers.GiveEventManager;
import it.fulminazzo.giveevent.managers.GiveLocationsManager;
import it.fulminazzo.giveevent.managers.ItemsManager;
import it.fulminazzo.giveevent.objects.Enchant;
import it.fulminazzo.giveevent.objects.GiveLocation;
import it.fulminazzo.giveevent.objects.Item;
import it.fulminazzo.yamlparser.objects.configurations.FileConfiguration;
import it.fulminazzo.yamlparser.objects.yamlelements.CallableYAMLParser;

public interface IGiveEvent extends IEventPlugin {

    @Override
    default void loadAll() throws Exception {
        addParsers();
        IEventPlugin.super.loadAll();
        setGiveEventManager(new GiveEventManager(this));
        ItemsManager itemsManager = new ItemsManager(this);
        setItemsManager(itemsManager);
        itemsManager.reloadAll();
        GiveLocationsManager giveLocationsManager = new GiveLocationsManager(this);
        setGiveLocationsManager(giveLocationsManager);
        giveLocationsManager.reloadAll();
        getCommand("giveevent").setExecutor(new GiveEventCommand(this));
    }

    @Override
    default void unloadAll() throws Exception {
        IEventPlugin.super.unloadAll();
        GiveEventManager giveEventManager = getGiveEventManager();
        if (giveEventManager != null) giveEventManager.stopEvent();
        ItemsManager itemsManager = getItemsManager();
        if (itemsManager != null) itemsManager.saveAll();
        GiveLocationsManager giveLocationsManager = getGiveLocationsManager();
        if (giveLocationsManager != null) giveLocationsManager.saveAll();
    }
    
    default void addParsers() {
        FileConfiguration.addParsers(
                new CallableYAMLParser<>(GiveLocation.class, (e) -> new GiveLocation()),
                new CallableYAMLParser<>(Enchant.class, (e) -> new Enchant()),
                new CallableYAMLParser<>(Item.class, (e) -> new Item(this))
        );
    }

    void setGiveEventManager(GiveEventManager eventManager);

    GiveEventManager getGiveEventManager();

    void setItemsManager(ItemsManager itemsManager);

    ItemsManager getItemsManager();

    void setGiveLocationsManager(GiveLocationsManager giveLocationsManager);

    GiveLocationsManager getGiveLocationsManager();
}
