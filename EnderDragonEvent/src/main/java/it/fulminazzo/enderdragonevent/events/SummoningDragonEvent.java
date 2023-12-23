package it.fulminazzo.enderdragonevent.events;

import it.fulminazzo.enderdragonevent.managers.EnderDragonManager;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SummoningDragonEvent extends EnderDragonManagerEvent {
    private String name;

    public SummoningDragonEvent(EnderDragonManager enderDragonManager, String name) {
        super(enderDragonManager);
        this.name = name;
    }
}