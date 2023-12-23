package it.fulminazzo.enderdragonevent.services;

import it.fulminazzo.enderdragonevent.enums.ConfigOption;
import it.fulminazzo.enderdragonevent.events.EnderDragonStartCounterEvent;
import it.fulminazzo.enderdragonevent.managers.EnderDragonManager;
import it.fulminazzo.events.interfaces.IEventPlugin;
import it.fulminazzo.events.tasks.TimerMessagesRunnable;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EnderDragonService {
    private final IEventPlugin plugin;
    private final List<EnderDragonManager> enderDragonManagers;
    private TimerMessagesRunnable timerMessagesRunnable;

    public EnderDragonService(IEventPlugin plugin) {
        this.plugin = plugin;
        this.enderDragonManagers = new ArrayList<>();
        Bukkit.getWorlds().stream().filter(w -> w.getEnvironment().equals(World.Environment.THE_END)).forEach(this::getEnderDragonManager);
    }

    public EnderDragonManager getEnderDragonManager(String worldName) {
        return worldName == null ? null : getEnderDragonManager(Bukkit.getWorld(worldName));
    }

    public EnderDragonManager getEnderDragonManager(World world) {
        if (world == null) return null;
        if (!world.getEnvironment().equals(World.Environment.THE_END)) return null;
        EnderDragonManager enderDragonManager = this.enderDragonManagers.stream().filter(e -> e.equals(world)).findFirst().orElse(null);
        if (enderDragonManager == null) {
            enderDragonManager = new EnderDragonManager(plugin, world);
            this.enderDragonManagers.add(enderDragonManager);
        }
        return enderDragonManager;
    }

    public void startTimer(World world) {
        Integer time = ConfigOption.ENDER_DRAGON_TIMER.getInteger();
        if (time == null || time < 1) time = 60;
        startTimer(world, time);
    }

    public void startTimer(World world, int time) {
        String name = ConfigOption.ENDER_DRAGON_NAME.getString();
        if (name == null) name = "EnderDragon";
        BarColor barColor = ConfigOption.ENDER_DRAGON_COLOR.getBarColor();
        BarStyle barStyle = ConfigOption.ENDER_DRAGON_STYLE.getBarStyle();
        List<BarFlag> barFlags = ConfigOption.ENDER_DRAGON_FLAGS.getBarFlags();
        startTimer(world, time, name, barColor, barStyle, barFlags.toArray(new BarFlag[0]));
    }

    public void startTimer(World world, int time, String name, BarColor barColor, BarStyle barStyle, BarFlag... barFlags) {
        stopTimer();
        EnderDragonManager enderDragonManager = getEnderDragonManager(world);
        if (enderDragonManager == null || !enderDragonManager.isIdle()) return;

        List<BarFlag> flags = new ArrayList<>(Arrays.asList(barFlags));
        EnderDragonStartCounterEvent event = new EnderDragonStartCounterEvent(enderDragonManager, time,
                name, barColor, barStyle, flags);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled() || event.getTime() < 1) return;
        time = event.getTime();
        if (event.getName() != null) name = event.getName();
        if (event.getBarColor() != null) barColor = event.getBarColor();
        if (event.getBarStyle() != null) barStyle = event.getBarStyle();
        flags.clear();
        flags.addAll(event.getBarFlags().stream().filter(Objects::nonNull).collect(Collectors.toList()));

        String finalName = name;
        BarColor finalBarColor = barColor;
        BarStyle finalBarStyle = barStyle;
        timerMessagesRunnable = new TimerMessagesRunnable(plugin, ConfigOption.MESSAGES.getPath(),
                p -> getEnderDragonManager(world).startRespawn(finalName, finalBarColor, finalBarStyle, flags.toArray(new BarFlag[0])),
                time);
        timerMessagesRunnable.start(false);
    }

    public void stopTimer() {
        if (timerMessagesRunnable != null) {
            if (!timerMessagesRunnable.isCancelled()) timerMessagesRunnable.cancel();
            timerMessagesRunnable = null;
        }
    }

    public boolean isTimerActive() {
        return timerMessagesRunnable != null && !timerMessagesRunnable.isCancelled();
    }

    public void stopAll() {
        this.enderDragonManagers.forEach(EnderDragonManager::stopAll);
        stopTimer();
    }
}