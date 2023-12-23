package it.fulminazzo.enderdragonevent.listeners;

import it.fulminazzo.enderdragonevent.enums.ConfigOption;
import it.fulminazzo.enderdragonevent.enums.PlayerStatistic;
import it.fulminazzo.enderdragonevent.events.DamageByDragonEvent;
import it.fulminazzo.enderdragonevent.events.DragonDamageEvent;
import it.fulminazzo.enderdragonevent.events.DragonDeathEvent;
import it.fulminazzo.enderdragonevent.interfaces.IEnderDragonEvent;
import it.fulminazzo.enderdragonevent.managers.EnderDragonManager;
import it.fulminazzo.enderdragonevent.objects.EnderPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.boss.DragonBattle;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class EnderDragonListener implements Listener {
    private final IEnderDragonEvent plugin;

    public EnderDragonListener(IEnderDragonEvent plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDragonDamage(DragonDamageEvent event) {
        event.getEnderDragonManager().getPlayer(event.getDamager()).increaseDamageDealt(event.getDamage());
    }

    @EventHandler
    public void onDragonDamage(DamageByDragonEvent event) {
        event.getEnderDragonManager().getPlayer(event.getDamaged()).increaseDamageReceived(event.getDamage());
    }

    @EventHandler
    public void onDragonDeath(DragonDeathEvent event) {
        EnderDragonManager enderDragonManager = event.getEnderDragonManager();
        EnderDragon enderDragon = event.getEnderDragon();
        World world = enderDragon.getWorld();
        Player killer = enderDragon.getKiller();
        if (killer != null) {
            List<String> rewards = ConfigOption.REWARDS.getStringList();
            if (rewards != null)
                rewards.stream().map(c -> c.replace("%killer%", killer.getName()))
                        .map(c -> {
                            for (int i = 1; i <= 10; i++) {
                                EnderPlayer enderPlayer = enderDragonManager.getPlayerStatistic(PlayerStatistic.DAMAGE_DEALT, i);
                                String name = enderPlayer == null ? "" : enderPlayer.getName();
                                String damage = enderPlayer == null ? "" : String.format("%.2f", enderPlayer.getDamageDealt());
                                c = c.replace("%player_damage_dealt_" + i + "%", name)
                                        .replace("%damage_dealt_" + i + "%", damage);
                                enderPlayer = enderDragonManager.getPlayerStatistic(PlayerStatistic.DAMAGE_RECEIVED, i);
                                name = enderPlayer == null ? "" : enderPlayer.getName();
                                damage = enderPlayer == null ? "" : String.format("%.2f", enderPlayer.getDamageReceived());
                                c = c.replace("%player_damage_received_" + i + "%", name)
                                        .replace("%damage_received_" + i + "%", damage);
                            }
                            return c;
                        })
                        .forEach(c -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), c));
        }
        if (((Plugin) plugin).isEnabled())
            Bukkit.getScheduler().runTask((Plugin) plugin, enderDragonManager::setDragonStatus);
        if (ConfigOption.PLACE_EGG.getBoolean()) {
            DragonBattle dragonBattle = world.getEnderDragonBattle();
            if (dragonBattle == null) return;
            Location portalLocation = dragonBattle.getEndPortalLocation();
            if (portalLocation == null) return;
            portalLocation.add(0, 4, 0);
            portalLocation.getBlock().setType(Material.DRAGON_EGG);
        }
    }
}