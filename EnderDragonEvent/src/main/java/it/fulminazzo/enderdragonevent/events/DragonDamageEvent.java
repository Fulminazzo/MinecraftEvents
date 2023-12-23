package it.fulminazzo.enderdragonevent.events;

import it.fulminazzo.enderdragonevent.managers.EnderDragonManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;

@Getter
@Setter
public class DragonDamageEvent extends EnderDragonManagerCancellableEvent {
    private final EnderDragon enderDragon;
    private final Player damager;
    private double damage;

    public DragonDamageEvent(EnderDragonManager enderDragonManager, EnderDragon enderDragon, Player damager, double damage) {
        super(enderDragonManager);
        this.enderDragon = enderDragon;
        this.damager = damager;
        this.damage = damage;
    }
}