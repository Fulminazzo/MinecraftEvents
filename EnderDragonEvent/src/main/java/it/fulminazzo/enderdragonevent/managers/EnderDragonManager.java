package it.fulminazzo.enderdragonevent.managers;

import it.fulminazzo.enderdragonevent.enums.DragonState;
import it.fulminazzo.enderdragonevent.enums.PlayerStatistic;
import it.fulminazzo.enderdragonevent.events.EnderDragonStartRespawnEvent;
import it.fulminazzo.enderdragonevent.events.SummonedDragonEvent;
import it.fulminazzo.enderdragonevent.events.SummoningDragonEvent;
import it.fulminazzo.enderdragonevent.exceptions.NotEndException;
import it.fulminazzo.enderdragonevent.objects.EnderPlayer;
import it.fulminazzo.enderdragonevent.tasks.RespawnRunnable;
import it.fulminazzo.events.interfaces.IEventPlugin;
import it.fulminazzo.events.tasks.RepeatingTask;
import it.fulminazzo.events.utils.StringUtils;
import it.fulminazzo.fulmicollection.exceptions.GeneralCannotBeNullException;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.DragonBattle;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.stream.Collectors;

public class EnderDragonManager {
    private final IEventPlugin plugin;
    @Getter
    private final String worldName;
    @Getter
    private final List<EnderPlayer> players;
    private RespawnRunnable respawnRunnable;
    @Getter
    private DragonState dragonStatus;

    public EnderDragonManager(IEventPlugin plugin, World world) {
        this.plugin = plugin;
        if (world == null) throw new GeneralCannotBeNullException("World for EnderDragon");
        if (!world.getEnvironment().equals(World.Environment.THE_END))
            throw new NotEndException(world);
        this.worldName = world.getName();
        this.players = new ArrayList<>();
        setDragonStatus();
    }

    public boolean isIdle() {
        return dragonStatus.equals(DragonState.DEAD);
    }

    public void startRespawn(String name, BarColor barColor, BarStyle barStyle, BarFlag... barFlags) {
        stopRespawn();
        World world = getWorld();
        if (world == null) return;

        List<BarFlag> flags = new ArrayList<>(Arrays.asList(barFlags));
        EnderDragonStartRespawnEvent event = new EnderDragonStartRespawnEvent(this, name, barColor, barStyle, flags);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;
        if (event.getName() != null) name = event.getName();
        if (event.getBarColor() != null) barColor = event.getBarColor();
        if (event.getBarStyle() != null) barStyle = event.getBarStyle();
        flags.clear();
        flags.addAll(event.getBarFlags().stream().filter(Objects::nonNull).collect(Collectors.toList()));

        String finalName = name;
        respawnRunnable = new RespawnRunnable(plugin, world, name, barColor, barStyle, barFlags, () -> {
            setDragonStatus();
            SummoningDragonEvent ev = new SummoningDragonEvent(this, finalName);
            Bukkit.getPluginManager().callEvent(ev);
            String n = ev.getName();
            if (n != null)
                new RepeatingTask(plugin, () -> !world.getEntitiesByClass(EnderDragon.class).isEmpty(),
                    () -> {
                        EnderDragon enderDragon = world.getEntitiesByClass(EnderDragon.class).stream().findFirst().orElse(null);
                        if (enderDragon == null) return;
                        enderDragon.setCustomName(StringUtils.parseMessage(n));
                        SummonedDragonEvent e = new SummonedDragonEvent(this, enderDragon);
                        Bukkit.getPluginManager().callEvent(e);
                }).start();
        }, this::setDragonStatus);
        respawnRunnable.runTaskTimer((Plugin) plugin, 0, 20);
        setDragonStatus();
    }

    public void stopRespawn() {
        if (respawnRunnable != null) {
            if (!respawnRunnable.isCancelled()) respawnRunnable.cancel();
            respawnRunnable = null;
        }
        setDragonStatus();
    }

    public void setDragonStatus() {
        World world = getWorld();
        if (world == null) return;
        DragonBattle dragonBattle = world.getEnderDragonBattle();
        if (!world.getEntitiesByClass(EnderDragon.class).isEmpty()) dragonStatus = DragonState.FIGHTING;
        else if (respawnRunnable != null && !respawnRunnable.isCancelled())
            dragonStatus = DragonState.SUMMONING_CRYSTALS;
        else if (dragonBattle != null && !dragonBattle.getRespawnPhase().equals(DragonBattle.RespawnPhase.NONE))
            dragonStatus = DragonState.SUMMONING;
        else {
            dragonStatus = DragonState.DEAD;
            players.clear();
        }
    }

    public EnderPlayer getPlayer(Player player) {
        if (player == null) return null;
        EnderPlayer enderPlayer = players.stream().filter(p -> p.equals(player)).findFirst().orElse(null);
        if (enderPlayer == null) {
            enderPlayer = new EnderPlayer(player);
            players.add(enderPlayer);
        }
        return enderPlayer;
    }

    public EnderPlayer getPlayerStatistic(PlayerStatistic sortStatistic, int position) {
        position--;
        if (position < 0 || players.size() - 1 < position) return null;
        return players.stream().sorted(Comparator.comparing(p -> {
            switch (sortStatistic) {
                case DAMAGE_DEALT: return p.getDamageDealt();
                case DAMAGE_RECEIVED: return p.getDamageReceived();
            }
            return 0.0;
        })).collect(Collectors.toList()).get(position);
    }

    public World getWorld() {
        return worldName == null ? null : Bukkit.getWorld(worldName);
    }

    public boolean equals(String string) {
        return string != null && string.equalsIgnoreCase(this.worldName);
    }

    public boolean equals(World world) {
        return world != null && equals(world.getName());
    }

    public void stopAll() {
        stopRespawn();
        World world = getWorld();
        if (world == null) return;
        world.getEntitiesByClass(EnderDragon.class).forEach(e -> e.setHealth(0));
        setDragonStatus();
    }
}