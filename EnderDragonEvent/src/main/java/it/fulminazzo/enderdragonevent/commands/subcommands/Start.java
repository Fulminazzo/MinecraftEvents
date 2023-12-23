package it.fulminazzo.enderdragonevent.commands.subcommands;

import it.fulminazzo.enderdragonevent.enums.ConfigOption;
import it.fulminazzo.enderdragonevent.enums.EDPermission;
import it.fulminazzo.enderdragonevent.enums.Message;
import it.fulminazzo.enderdragonevent.interfaces.IEnderDragonEvent;
import it.fulminazzo.enderdragonevent.managers.EnderDragonManager;
import it.fulminazzo.enderdragonevent.services.EnderDragonService;
import it.fulminazzo.events.commands.EventCommand;
import it.fulminazzo.events.commands.subcommands.EventSubCommand;
import it.fulminazzo.events.interfaces.IEventPlugin;
import it.fulminazzo.events.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.generator.WorldInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Start extends EventSubCommand {

    public Start(IEventPlugin plugin, EventCommand command) {
        super(plugin, command, "start", EDPermission.START,
                Message.START_DRAGON_EVENT_DESCRIPTION.getMessage(true),
                "start <world> <time> <name> <color> <style> <flags...>");
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        World world;
        int time = ConfigOption.ENDER_DRAGON_TIMER.getInteger();
        String name = ConfigOption.ENDER_DRAGON_NAME.getString();
        if (name == null) name = "EnderDragon";
        BarColor barColor = ConfigOption.ENDER_DRAGON_COLOR.getBarColor();
        BarStyle barStyle = ConfigOption.ENDER_DRAGON_STYLE.getBarStyle();
        List<BarFlag> barFlags = ConfigOption.ENDER_DRAGON_FLAGS.getBarFlags();

        if (args.length == 0) {
            if (sender instanceof Player) world = ((Player) sender).getWorld();
            else {
                sender.sendMessage(Message.NOT_ENOUGH_ARGUMENTS.getMessage(true));
                sender.sendMessage(Message.USAGE.getMessage(true).replace("%usage%", getUsage()));
                return;
            }
        } else world = Bukkit.getWorld(args[0]);

        if (world == null) {
            sender.sendMessage(Message.WORLD_NOT_FOUND.getMessage(true).replace("%world%", args[0]));
            return;
        }
        if (!world.getEnvironment().equals(World.Environment.THE_END)) {
            sender.sendMessage(Message.WORLD_NOT_END.getMessage(true).replace("%world%", args[0]));
            return;
        }
        EnderDragonService enderDragonService = ((IEnderDragonEvent) plugin).getEnderDragonService();
        EnderDragonManager enderDragonManager = enderDragonService.getEnderDragonManager(world);
        if (!enderDragonManager.isIdle()) {
            sender.sendMessage(Message.ENDER_DRAGON_EVENT_ALREADY_RUNNING.getMessage(true).replace("%world%", world.getName()));
            return;
        }
        if (!world.getEntitiesByClass(EnderDragon.class).isEmpty()) {
            sender.sendMessage(Message.ENDER_DRAGON_STILL_ALIVE.getMessage(true).replace("%world%", world.getName()));
            return;
        }

        if (args.length > 1) {
            barFlags.clear();
            try {
                time = Integer.parseInt(args[1]);
                if (time < 1) throw new IllegalArgumentException();
            } catch (IllegalArgumentException e) {
                sender.sendMessage(Message.NOT_VALID_NUMBER.getMessage(true)
                        .replace("%num%", args[1]));
                return;
            }
        }

        if (args.length > 2) name = args[2];

        if (args.length > 3) {
            try {
                barColor = BarColor.valueOf(args[3].toUpperCase());
            } catch (IllegalArgumentException e) {
                sender.sendMessage(Message.NOT_VALID_ENUM.getMessage(true)
                        .replace("%arg%", args[3]).replace("%enum%", BarColor.class.getSimpleName()));
                return;
            }
        }

        if (args.length > 4) {
            try {
                barStyle = BarStyle.valueOf(args[4].toUpperCase());
            } catch (IllegalArgumentException e) {
                sender.sendMessage(Message.NOT_VALID_ENUM.getMessage(true)
                        .replace("%arg%", args[4]).replace("%enum%", BarStyle.class.getSimpleName()));
                return;
            }
        }

        if (args.length > 5) {
            barFlags.clear();
            for (int i = 5; i < args.length; i++) {
                try {
                    barFlags.add(BarFlag.valueOf(args[i].toUpperCase()));
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(Message.NOT_VALID_ENUM.getMessage(true)
                            .replace("%arg%", args[4]).replace("%enum%", BarFlag.class.getSimpleName()));
                    return;
                }
            }
        }
        name = StringUtils.parseMessage(name);

        enderDragonService.startTimer(world, time, name, barColor, barStyle, barFlags.toArray(new BarFlag[0]));
        sender.sendMessage(Message.SPAWNING_ENDER_DRAGON.getMessage(true)
                .replace("%world%", world.getName())
                .replace("%time%", String.valueOf(time))
                .replace("%name%", name)
                .replace("%color%", barColor.name())
                .replace("%style%", barStyle.name())
                .replace("%flags%", barFlags.stream().map(Enum::name).collect(Collectors.joining("&8, &6")))
        );
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1)
            list.addAll(Bukkit.getWorlds().stream()
                    .filter(w -> w.getEnvironment().equals(World.Environment.THE_END))
                    .map(WorldInfo::getName).collect(Collectors.toList()));
        if (args.length == 2) list.add("<time>");
        if (args.length == 3) list.add("<name>");
        if (args.length == 4) list.addAll(Arrays.stream(BarColor.values()).map(Enum::name).map(String::toLowerCase).collect(Collectors.toList()));
        if (args.length == 5) list.addAll(Arrays.stream(BarStyle.values()).map(Enum::name).map(String::toLowerCase).collect(Collectors.toList()));
        if (args.length >= 6) list.addAll(Arrays.stream(BarFlag.values())
                .map(Enum::name)
                .map(String::toLowerCase)
                .filter(s -> Arrays.stream(Arrays.copyOfRange(args, 5, args.length)).noneMatch(a -> a.equalsIgnoreCase(s)))
                .collect(Collectors.toList()));
        return list;
    }

    @Override
    public int getMinArguments() {
        return 0;
    }
}
