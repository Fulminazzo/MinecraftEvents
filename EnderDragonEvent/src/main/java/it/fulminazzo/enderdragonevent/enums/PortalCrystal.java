package it.fulminazzo.enderdragonevent.enums;

import com.google.common.collect.Iterables;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.boss.DragonBattle;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.Collection;

public enum PortalCrystal {
    NORTH_CRYSTAL(0, -3),
    EAST_CRYSTAL(3, 0),
    SOUTH_CRYSTAL(0, 3),
    WEST_CRYSTAL(-3, 0);

    private final int xOffset, zOffset;

    PortalCrystal(int xOffset, int zOffset) {
        this.xOffset = xOffset;
        this.zOffset = zOffset;
    }

    public Location getRelativeToPortal(World world) {
        if (world == null) return null;
        if (!world.getEnvironment().equals(Environment.THE_END)) return null;

        DragonBattle dragonBattle = world.getEnderDragonBattle();
        if (dragonBattle == null) return null;
        dragonBattle.generateEndPortal(false);

        return getRelativeTo(dragonBattle.getEndPortalLocation());
    }

    public void spawn(World world) {
        spawn(world, true);
    }

    public void spawn(World world, boolean invulnerable) {
        if (world == null) return;
        if (!world.getEnvironment().equals(Environment.THE_END)) return;

        DragonBattle dragonBattle = world.getEnderDragonBattle();
        if (dragonBattle == null) return;

        dragonBattle.generateEndPortal(false);
        Location endPortalLocation = dragonBattle.getEndPortalLocation();
        if (endPortalLocation == null) return;

        Location endCrystalLocation = getRelativeTo(endPortalLocation).add(0.5, 0, 0.5);

        Collection<Entity> entities = world.getNearbyEntities(endCrystalLocation, 1, 1, 1);
        EnderCrystal enderCrystal = (EnderCrystal) entities.stream().filter(e -> e instanceof EnderCrystal).findFirst().orElse(null);
        if (enderCrystal == null) {
            enderCrystal = (EnderCrystal) world.spawnEntity(endCrystalLocation, EntityType.ENDER_CRYSTAL);
            enderCrystal.setInvulnerable(invulnerable);
            enderCrystal.setShowingBottom(false);
        }
    }

    public Location getRelativeTo(Location location) {
        if (location == null) return null;
        return location.add(xOffset, 1, zOffset);
    }

    public EnderCrystal get(World world) {
        if (world == null) return null;

        DragonBattle dragonBattle = world.getEnderDragonBattle();
        if (dragonBattle == null) return null;

        dragonBattle.generateEndPortal(false);
        Location endPortalLocation = dragonBattle.getEndPortalLocation();
        Location location = getRelativeTo(endPortalLocation);
        if (location == null) return null;
        Collection<Entity> entities = world.getNearbyEntities(location, 1, 1, 1);
        return (EnderCrystal) Iterables.find(entities, e -> e instanceof EnderCrystal, null);
    }
}
