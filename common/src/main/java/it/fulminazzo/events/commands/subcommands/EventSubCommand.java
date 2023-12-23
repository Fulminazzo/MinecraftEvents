package it.fulminazzo.events.commands.subcommands;

import it.fulminazzo.events.EventPlugin;
import it.fulminazzo.events.commands.EventCommand;
import it.fulminazzo.events.interfaces.EventPermission;
import it.fulminazzo.events.interfaces.IEventPlugin;
import it.fulminazzo.events.utils.StringUtils;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public abstract class EventSubCommand {
    protected final IEventPlugin plugin;
    private final String name;
    private final String permission;
    private final String description;
    private final String usage;
    private final String[] aliases;

    public EventSubCommand(IEventPlugin plugin, EventCommand command, String name, EventPermission permission, String description, String usage, String... aliases) {
        this.plugin = plugin;
        this.name = name;
        this.permission = permission.getPermission();
        this.description = description;
        this.usage = formatUsage((command == null ? "" : (command.getName() + " ")) + usage);
        this.aliases = aliases == null ? new String[0] : aliases;
    }

    public abstract void execute(CommandSender sender, Command cmd, String label, String[] args);

    public abstract List<String> tabComplete(CommandSender sender, Command cmd, String label, String[] args);

    public abstract int getMinArguments();

    public boolean equals(String name) {
        if (name == null) return false;
        return this.name.equalsIgnoreCase(name) || Arrays.stream(this.aliases).anyMatch(s -> s.equalsIgnoreCase(name));
    }

    private String formatUsage(String usage) {
        if (usage == null) return null;
        Matcher matcher = Pattern.compile("<([^>]+)>").matcher(usage);
        while (matcher.find())
            usage = usage.replace(matcher.group(), String.format("&8<&e%s&8>", matcher.group(1)));
        return StringUtils.parseMessage(usage);
    }
}
