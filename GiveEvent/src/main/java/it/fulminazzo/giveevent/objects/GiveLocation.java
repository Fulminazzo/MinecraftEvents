package it.fulminazzo.giveevent.objects;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

@Getter
public class GiveLocation {
    private String name;
    private String worldName;
    private Integer x;
    private Integer y;
    private Integer z;

    public GiveLocation() {
    }

    public GiveLocation(String name, Location location) {
        this(name, location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public GiveLocation(String name, World world, int x, int y, int z) {
        this(name, world == null ? null : world.getName(), x, y, z);
    }

    public GiveLocation(String name, String worldName, int x, int y, int z) {
        this.name = name;
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setLocation(Location location) {
        if (location == null || location.getWorld() == null) return;
        this.worldName = location.getWorld().getName();
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
    }

    public Location toBukkit() {
        if (worldName == null) return null;
        World world = Bukkit.getWorld(worldName);
        if (world == null) return null;
        return new Location(world, x, y, z);
    }

    @Override
    public String toString() {
        return String.format("%s (%s) {%s, x: %s, y: %s, z: %s}", getClass().getSimpleName(),
                name, worldName, x, y, z);
    }
}