package it.fulminazzo.events.tasks;

import it.fulminazzo.events.interfaces.IEventPlugin;
import it.fulminazzo.events.utils.StringUtils;
import it.fulminazzo.yamlparser.objects.configurations.ConfigurationSection;
import it.fulminazzo.yamlparser.objects.configurations.FileConfiguration;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.function.Consumer;

public class TimerMessagesRunnable extends TimerRunnable {

    public TimerMessagesRunnable(IEventPlugin plugin, String messagesPath, Consumer<IEventPlugin> action, int time) {
        super(plugin, (p, c) -> {
            FileConfiguration configuration = p.getConfiguration();
            if (configuration == null) return;
            ConfigurationSection messagesSection = configuration.getConfigurationSection(messagesPath);
            String sectionName = String.valueOf(c);
            if (messagesSection == null) return;
            if (!messagesSection.contains(sectionName)) return;
            messagesSection.contains(sectionName);
            List<String> messages = messagesSection.getStringList(sectionName);
            if (messages != null)
                messages.stream()
                        .map(m -> m.replace("%time%", String.valueOf(c)))
                        .map(StringUtils::parseMessage)
                        .forEach(Bukkit::broadcastMessage);
        }, action, time);
    }
}
