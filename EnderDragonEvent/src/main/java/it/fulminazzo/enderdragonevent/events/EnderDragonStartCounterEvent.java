package it.fulminazzo.enderdragonevent.events;

import it.fulminazzo.enderdragonevent.managers.EnderDragonManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;

import java.util.List;

@Getter
@Setter
public class EnderDragonStartCounterEvent extends EnderDragonManagerCancellableEvent {
    private int time;
    private String name;
    private BarColor barColor;
    private BarStyle barStyle;
    private final List<BarFlag> barFlags;

    public EnderDragonStartCounterEvent(EnderDragonManager enderDragonManager, int time, String name, BarColor barColor, BarStyle barStyle, List<BarFlag> barFlags) {
        super(enderDragonManager);
        this.time = time;
        this.name = name;
        this.barColor = barColor;
        this.barStyle = barStyle;
        this.barFlags = barFlags;
    }
}