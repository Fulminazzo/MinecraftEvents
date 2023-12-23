package it.fulminazzo.enderdragonevent.events;

import it.fulminazzo.enderdragonevent.managers.EnderDragonManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.EnderDragon;

@Getter
@Setter
public class DragonDeathEvent extends EnderDragonManagerEvent {
    private final EnderDragon enderDragon;

    public DragonDeathEvent(EnderDragonManager enderDragonManager, EnderDragon enderDragon) {
        super(enderDragonManager);
        this.enderDragon = enderDragon;
    }
}