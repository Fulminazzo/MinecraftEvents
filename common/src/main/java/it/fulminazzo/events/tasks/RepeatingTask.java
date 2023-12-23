package it.fulminazzo.events.tasks;

import it.fulminazzo.events.interfaces.IEventPlugin;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Supplier;

/**
 * A Task that gets executed until a certain
 * condition is met.
 */
public class RepeatingTask extends BukkitRunnable {
    private final IEventPlugin plugin;
    private final Supplier<Boolean> condition;
    private final Runnable action;

    public RepeatingTask(IEventPlugin plugin, Supplier<Boolean> condition, Runnable action) {
        this.plugin = plugin;
        this.condition = condition;
        this.action = action;
    }

    public void start() {
        runTaskTimer((Plugin) plugin, 0, 1);
    }

    @Override
    public void run() {
        Plugin p = (Plugin) plugin;
        if (!p.isEnabled()) return;
        if (condition == null || action == null) return;
        if (condition.get()) {
            action.run();
            cancel();
        }
    }
}
