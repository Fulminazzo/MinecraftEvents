package it.fulminazzo.enderdragonevent;

import it.fulminazzo.enderdragonevent.interfaces.IEnderDragonEvent;
import it.fulminazzo.enderdragonevent.services.EnderDragonService;
import it.fulminazzo.events.EventPlugin;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class EnderDragonEvent extends EventPlugin implements IEnderDragonEvent {
    private EnderDragonService enderDragonService;

    public EnderDragonEvent() {
        super();
        plugin = this;
    }

    @Override
    public String getDisplayName() {
        return "&5EnderDragon&6Event";
    }
}
