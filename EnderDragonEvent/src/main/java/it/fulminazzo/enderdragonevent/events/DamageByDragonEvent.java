package it.fulminazzo.enderdragonevent.events;

import it.fulminazzo.enderdragonevent.managers.EnderDragonManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;

@Getter
@Setter
public class DamageByDragonEvent extends EnderDragonManagerCancellableEvent {
    private final EnderDragon enderDragon;
    private final Player damaged;
    private double damage;

    public DamageByDragonEvent(EnderDragonManager enderDragonManager, Player damaged, EnderDragon enderDragon, double damage) {
        super(enderDragonManager);
        this.enderDragon = enderDragon;
        this.damaged = damaged;
        this.damage = damage;
    }
}