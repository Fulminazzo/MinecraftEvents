package it.fulminazzo.enderdragonevent.listeners;

import it.fulminazzo.enderdragonevent.events.DamageByDragonEvent;
import it.fulminazzo.enderdragonevent.events.DragonDamageEvent;
import it.fulminazzo.enderdragonevent.events.DragonDeathEvent;
import it.fulminazzo.enderdragonevent.interfaces.IEnderDragonEvent;
import it.fulminazzo.enderdragonevent.managers.EnderDragonManager;
import it.fulminazzo.enderdragonevent.services.EnderDragonService;
import it.fulminazzo.reflectionutils.objects.ReflObject;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class RawEnderDragonListener implements Listener {
    private final IEnderDragonEvent plugin;

    public RawEnderDragonListener(IEnderDragonEvent plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity damager = event.getDamager();
        EnderDragonService enderDragonService = plugin.getEnderDragonService();
        EnderDragonManager enderDragonManager = enderDragonService.getEnderDragonManager(entity.getWorld());
        if (enderDragonManager == null) return;
        Cancellable dragonEvent;
        if (entity instanceof EnderDragon && damager instanceof Player)
            dragonEvent = new DragonDamageEvent(enderDragonManager, (EnderDragon) entity, (Player) damager, event.getDamage());
        else if (entity instanceof Player && damager instanceof EnderDragon)
            dragonEvent = new DamageByDragonEvent(enderDragonManager, (Player) entity, (EnderDragon) damager, event.getDamage());
        else return;
        Bukkit.getPluginManager().callEvent((Event) dragonEvent);
        event.setCancelled(dragonEvent.isCancelled());
        event.setDamage(new ReflObject<>(dragonEvent).getMethodObject("getDamage"));
    }

    @EventHandler
    public void onEnderDragonDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof EnderDragon)) return;
        World world = entity.getWorld();
        EnderDragonService enderDragonService = plugin.getEnderDragonService();
        EnderDragonManager enderDragonManager = enderDragonService.getEnderDragonManager(world);
        if (enderDragonManager == null) return;
        DragonDeathEvent dragonDeathEvent = new DragonDeathEvent(enderDragonManager, (EnderDragon) entity);
        Bukkit.getPluginManager().callEvent(dragonDeathEvent);
    }

    @EventHandler
    public void onAdvancement(PlayerAdvancementDoneEvent event) {
        Player player = event.getPlayer();
        Advancement advancement = event.getAdvancement();
        NamespacedKey keyedAdvancement = advancement.getKey();
        if (!keyedAdvancement.equals(NamespacedKey.minecraft("end/respawn_dragon"))) return;
        EnderDragonService enderDragonService = plugin.getEnderDragonService();
        EnderDragonManager enderDragonManager = enderDragonService.getEnderDragonManager(player.getWorld());
        if (enderDragonManager.isIdle()) return;
        advancement.getCriteria().forEach(c ->
                player.getAdvancementProgress(advancement).revokeCriteria(c));
    }
}
