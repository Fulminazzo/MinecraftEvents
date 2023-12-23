package it.fulminazzo.events.commands;

import it.fulminazzo.events.commands.subcommands.EventSubCommand;
import it.fulminazzo.events.interfaces.IEventPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class EventSubCommandable extends EventCommand implements TabExecutor {
    protected final List<EventSubCommand> subCommands;

    public EventSubCommandable(IEventPlugin plugin, String name) {
        super(plugin, name);
        this.subCommands = new ArrayList<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(notEnoughArguments());
            sender.sendMessage(usage().replace("%usage%", getUsage()));
            return true;
        }
        EventSubCommand subCommand = subCommands.stream().filter(c -> c.equals(args[0])).findFirst().orElse(null);
        if (subCommand == null)
            sender.sendMessage(subCommandNotFound().replace("%subcommand%", args[0]));
        else if (!sender.hasPermission(subCommand.getPermission()))
            sender.sendMessage(notEnoughPermissions());
        else if (args.length - 1 < subCommand.getMinArguments()) {
            sender.sendMessage(notEnoughArguments());
            sender.sendMessage(usage().replace("%usage%", subCommand.getUsage()));
        } else subCommand.execute(sender, cmd, label, Arrays.copyOfRange(args, 1, args.length));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1)
            list.addAll(subCommands.stream().filter(c -> sender.hasPermission(c.getPermission()))
                    .flatMap(s -> Stream.concat(Stream.of(s.getName()), Arrays.stream(s.getAliases())))
                    .collect(Collectors.toList()));
        if (args.length > 1) {
            EventSubCommand subCommand = subCommands.stream().filter(c -> c.equals(args[0])).findFirst().orElse(null);
            if (subCommand != null && sender.hasPermission(subCommand.getPermission())) {
                List<String> l = subCommand.tabComplete(sender, cmd, label, Arrays.copyOfRange(args, 1, args.length));
                if (l != null) list.addAll(l);
            }
        }
        return list.stream().filter(s -> s.toLowerCase().startsWith(args[args.length - 1].toLowerCase())).collect(Collectors.toList());
    }

    public void addSubCommands(EventSubCommand... subCommands) {
        if (subCommands != null) this.subCommands.addAll(List.of(subCommands));
    }

    public String getUsage() {
        return String.format("%s <subcommand>", getName());
    }

    public abstract String subCommandNotFound();

    public abstract String notEnoughArguments();

    public abstract String notEnoughPermissions();

    public abstract String usage();
}