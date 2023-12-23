package it.fulminazzo.events.tasks;

import it.fulminazzo.events.interfaces.IEventPlugin;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class TimerRunnable extends BukkitRunnable {
    private final IEventPlugin plugin;
    private final BiConsumer<IEventPlugin, Integer> timerFunction;
    private final Consumer<IEventPlugin> action;
    private int time;

    public TimerRunnable(IEventPlugin plugin, BiConsumer<IEventPlugin, Integer> timerFunction, Consumer<IEventPlugin> action, int time) {
        this.plugin = plugin;
        this.timerFunction = timerFunction;
        this.action = action;
        this.time = time;
    }

    public BukkitTask start() {
        return start(true);
    }

    public BukkitTask start(boolean async) {
        if (async) return runTaskTimerAsynchronously((Plugin) plugin, 0, 20);
        else return runTaskTimer((Plugin) plugin, 0, 20);
    }

    @Override
    public void run() {
        if (timerFunction != null) timerFunction.accept(plugin, time);
        if (time < 1) {
            if (action != null) action.accept(plugin);
            cancel();
        } else time--;
    }
}
