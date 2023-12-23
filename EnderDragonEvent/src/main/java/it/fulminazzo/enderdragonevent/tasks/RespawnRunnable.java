package it.fulminazzo.enderdragonevent.tasks;

import it.fulminazzo.enderdragonevent.enums.PortalCrystal;
import it.fulminazzo.enderdragonevent.exceptions.NotEndException;
import it.fulminazzo.events.enums.EventLog;
import it.fulminazzo.events.interfaces.IEventPlugin;
import it.fulminazzo.events.utils.StringUtils;
import org.bukkit.*;
import org.bukkit.boss.*;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EnderDragon;
import org.bukkit.scheduler.BukkitRunnable;

public class RespawnRunnable extends BukkitRunnable {
    private final IEventPlugin plugin;
    private final World endWorld;
    private final DragonBattle dragonBattle;
    private final Runnable success;
    private final Runnable error;

    private final String name;
    private final BarColor barColor;
    private final BarStyle barStyle;
    private final BarFlag[] barFlags;

    private int currentCrystal = 0;

    public RespawnRunnable(IEventPlugin plugin, World endWorld,
                           String name, BarColor barColor, BarStyle barStyle, BarFlag[] barFlags) throws NotEndException {
        this(plugin, endWorld, name, barColor, barStyle, barFlags, null, null);
    }

    public RespawnRunnable(IEventPlugin plugin, World endWorld,
                           String name, BarColor barColor, BarStyle barStyle, BarFlag[] barFlags,
                           Runnable success, Runnable error) throws NotEndException {
        this.plugin = plugin;
        this.endWorld = endWorld;
        this.success = success;
        this.error = error;
        this.name = name;
        this.barColor = barColor;
        this.barStyle = barStyle;
        this.barFlags = barFlags;

        if (!endWorld.getEnvironment().equals(World.Environment.THE_END)) throw new NotEndException(endWorld);
        this.dragonBattle = endWorld.getEnderDragonBattle();
    }

    @Override
    public void run() {
        if (endWorld.getPlayers().isEmpty()) return;

        PortalCrystal crystalPos = PortalCrystal.values()[currentCrystal++];
        Location crystalLocation = crystalPos.getRelativeToPortal(endWorld);
        if (crystalLocation == null) return;

        Chunk crystalChunk = endWorld.getChunkAt(crystalLocation);
        if (!crystalChunk.isLoaded()) crystalChunk.load();

        EnderCrystal existingCrystal = crystalPos.get(this.endWorld);
        if (existingCrystal != null) existingCrystal.remove();

        crystalPos.spawn(this.endWorld);
        endWorld.createExplosion(crystalLocation.getX(), crystalLocation.getY(), crystalLocation.getZ(), 0F, false, false);
        endWorld.spawnParticle(Particle.EXPLOSION_HUGE, crystalLocation, 0);

        if (currentCrystal < 4) return;

        EnderDragon enderDragon = endWorld.getEntitiesByClass(EnderDragon.class).stream().findAny().orElse(null);
        if (enderDragon == null) {
            dragonBattle.initiateRespawn();

            BossBar bossBar = dragonBattle.getBossBar();
            if (name != null) bossBar.setTitle(StringUtils.parseMessage(name));
            if (barColor != null) bossBar.setColor(barColor);
            if (barStyle != null) bossBar.setStyle(barStyle);
            if (barFlags != null) for (BarFlag b : barFlags) if (b != null) bossBar.addFlag(b);

            if (success != null) success.run();
        } else {
            plugin.warning(EventLog.ENDER_DRAGON_ALREADY_PRESENT, "%world%", this.endWorld.getName());

            for (PortalCrystal portalCrystal : PortalCrystal.values()) {
                Location location = portalCrystal.getRelativeToPortal(this.endWorld);
                if (location == null) continue;

                EnderCrystal enderCrystal = portalCrystal.get(this.endWorld);
                if (enderCrystal != null) enderCrystal.remove();

                endWorld.getPlayers().forEach(p -> p.playSound(location, Sound.BLOCK_FIRE_EXTINGUISH, 1000, 1));
                endWorld.createExplosion(location.getX(), location.getY(), location.getZ(), 0F, false, false);
            }

            if (error != null) error.run();
        }

        endWorld.getEntitiesByClass(EnderCrystal.class).forEach(c -> {
            c.setInvulnerable(false);
            c.setBeamTarget(null);
        });

        this.cancel();
    }
}
