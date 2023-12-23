package it.fulminazzo.enderdragonevent.events;

import it.fulminazzo.enderdragonevent.managers.EnderDragonManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;

@Getter
@Setter
abstract class EnderDragonManagerCancellableEvent extends EnderDragonManagerEvent implements Cancellable {
    private boolean cancelled;

    public EnderDragonManagerCancellableEvent(EnderDragonManager enderDragonManager) {
        super(enderDragonManager);
        this.cancelled = false;
    }
}
