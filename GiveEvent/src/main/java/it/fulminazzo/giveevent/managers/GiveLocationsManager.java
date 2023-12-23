package it.fulminazzo.giveevent.managers;

import it.fulminazzo.events.EventPlugin;
import it.fulminazzo.events.interfaces.IEventPlugin;
import it.fulminazzo.giveevent.objects.GiveLocation;
import it.fulminazzo.yamlparser.objects.configurations.FileConfiguration;
import it.fulminazzo.yamlparser.utils.FileUtils;
import lombok.Getter;
import org.bukkit.Location;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GiveLocationsManager {
    private final File locationsFile;
    @Getter
    private final List<GiveLocation> locations;

    public GiveLocationsManager(IEventPlugin plugin) {
        this.locationsFile = new File(plugin.getDataFolder(), "locations.yml");
        this.locations = new ArrayList<>();
    }

    public void reloadAll() {
        if (!this.locationsFile.isFile()) return;
        FileConfiguration configuration = new FileConfiguration(locationsFile);
        this.locations.addAll(configuration.getList("locations", GiveLocation.class));
    }

    public GiveLocation getRandomLocation() {
        if (locations.isEmpty()) return null;
        return locations.get(new Random().nextInt(locations.size()));
    }

    public GiveLocation getLocation(String name) {
        return this.locations.stream().filter(l -> l.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public GiveLocation add(String name, Location location) {
        if (name != null && location != null) {
            GiveLocation giveLocation = new GiveLocation(name, location);
            add(giveLocation);
            return giveLocation;
        }
        return null;
    }

    public void add(GiveLocation giveLocation) {
        this.locations.add(giveLocation);
    }

    public void remove(GiveLocation giveLocation) {
        if (giveLocation != null) remove(giveLocation.getName());
    }

    public void remove(String name) {
        if (name != null) this.locations.removeIf(l -> l.getName().equalsIgnoreCase(name));
    }

    public void saveAll() throws IOException {
        if (!this.locationsFile.isFile()) FileUtils.createNewFile(this.locationsFile);
        FileConfiguration configuration = new FileConfiguration(locationsFile);
        configuration.set("locations", locations);
        configuration.save();
    }
}