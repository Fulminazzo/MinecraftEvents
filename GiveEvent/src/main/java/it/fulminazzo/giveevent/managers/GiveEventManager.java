package it.fulminazzo.giveevent.managers;

import it.fulminazzo.events.interfaces.IEventPlugin;
import it.fulminazzo.events.tasks.TimerMessagesRunnable;
import it.fulminazzo.giveevent.enums.ConfigOption;
import it.fulminazzo.giveevent.enums.GiveStrategy;
import it.fulminazzo.giveevent.interfaces.IGiveEvent;
import it.fulminazzo.giveevent.objects.GiveLocation;
import it.fulminazzo.giveevent.objects.Item;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GiveEventManager {
    private final IEventPlugin plugin;
    private int counter;
    private BukkitTask giveTask;

    public GiveEventManager(IEventPlugin plugin) {
        this.plugin = plugin;
    }

    public void startEvent(int time) {
        this.counter = time;
        giveTask = new TimerMessagesRunnable(plugin, ConfigOption.MESSAGES.getPath(), p -> startGive(), 10).start();
    }

    protected void startGive() {
        stopEvent();
        GiveLocationsManager locationsManager = ((IGiveEvent) plugin).getGiveLocationsManager();
        if (locationsManager == null) return;
        ItemsManager itemsManager = ((IGiveEvent) plugin).getItemsManager();
        if (itemsManager == null) return;
        List<Item> items = new ArrayList<>(itemsManager.getItems());
        GiveStrategy dropStrategy = ConfigOption.DROP_STRATEGY.getGiveStrategy();
        final int[] i = {0};
        giveTask = Bukkit.getScheduler().runTaskTimer((Plugin) plugin, () -> {
            if (items.size() < i[0] + 1) stopEvent();
            else {
                List<GiveLocation> locations = null;
                switch (dropStrategy) {
                    case ALL: {
                        locations = locationsManager.getLocations();
                        break;
                    }
                    case RANDOM: {
                        locations = Collections.singletonList(locationsManager.getRandomLocation());
                    }
                }
                if (locations == null) return;
                locations.stream().map(GiveLocation::toBukkit)
                        .filter(l -> l.getWorld() != null)
                        .forEach(l -> l.getWorld().dropItemNaturally(l, items.get(i[0]).toBukkit()));
                i[0]++;
            }
        }, 0, Math.max(1, ConfigOption.DROP_INTERVAL.getInteger()));
    }

    public void stopEvent() {
        if (giveTask != null) {
            Bukkit.getScheduler().cancelTask(giveTask.getTaskId());
            giveTask = null;
        }
        counter = 0;
    }

    public boolean isRunning() {
        return counter > 0;
    }
}