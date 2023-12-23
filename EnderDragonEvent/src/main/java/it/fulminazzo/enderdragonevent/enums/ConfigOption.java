package it.fulminazzo.enderdragonevent.enums;

import it.fulminazzo.events.interfaces.IConfigOption;
import lombok.Getter;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum ConfigOption implements IConfigOption {
    ENDER_DRAGON_NAME("ender-dragon-event.ender-dragon.name"),
    ENDER_DRAGON_COLOR("ender-dragon-event.ender-dragon.color"),
    ENDER_DRAGON_STYLE("ender-dragon-event.ender-dragon.style"),
    ENDER_DRAGON_FLAGS("ender-dragon-event.ender-dragon.flags"),

    REWARDS("ender-dragon-event.rewards"),
    PLACE_EGG("ender-dragon-event.place-egg"),
    ENDER_DRAGON_TIMER("ender-dragon-event.timer"),
    MESSAGES("ender-dragon-event.messages"),
    ;

    private final String path;

    ConfigOption(String path) {
        this.path = path;
    }

    public BarColor getBarColor() {
        BarColor barColor;
        try {
            barColor = BarColor.valueOf(getString().toUpperCase());
        } catch (Exception e) {
            e.printStackTrace();
            barColor = BarColor.PURPLE;
        }
        return barColor;
    }

    public BarStyle getBarStyle() {
        BarStyle barStyle;
        try {
            barStyle = BarStyle.valueOf(ConfigOption.ENDER_DRAGON_STYLE.getString().toUpperCase());
        } catch (Exception e) {
            e.printStackTrace();
            barStyle = BarStyle.SEGMENTED_6;
        }
        return barStyle;
    }

    public List<BarFlag> getBarFlags() {
        List<BarFlag> barFlags = new ArrayList<>();
        for (String s : ConfigOption.ENDER_DRAGON_FLAGS.getStringList())
            try {barFlags.add(BarFlag.valueOf(s.toUpperCase()));}
            catch (IllegalArgumentException ignored) {}
        return barFlags;
    }
}