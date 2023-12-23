package it.fulminazzo.giveevent.commands.subcommands;

import it.fulminazzo.events.EventPlugin;
import it.fulminazzo.events.commands.EventCommand;
import it.fulminazzo.events.commands.subcommands.EventSubCommand;
import it.fulminazzo.events.interfaces.IEventPlugin;
import it.fulminazzo.giveevent.enums.GEPermission;
import it.fulminazzo.giveevent.enums.Message;
import it.fulminazzo.giveevent.interfaces.IGiveEvent;
import it.fulminazzo.giveevent.managers.GiveLocationsManager;
import it.fulminazzo.giveevent.objects.GiveLocation;
import it.fulminazzo.giveevent.utils.LocationUtils;
import it.fulminazzo.reflectionutils.objects.ReflObject;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SetGiveLocation extends EventSubCommand {

    public SetGiveLocation(IEventPlugin plugin, EventCommand command) {
        super(plugin, command, "setgivelocation", GEPermission.SET_GIVE_LOCATION,
                Message.SET_GIVE_LOCATION_DESCRIPTION.getMessage(false),
                "setgivelocation <name> <world> <x> <y> <z>", "setlocation", "setgl", "setl");
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        String name = args[0];
        Location location;
        if (args.length == 1) {
            if (sender instanceof Player) location = ((Player) sender).getLocation();
            else {
                sender.sendMessage(Message.NOT_ENOUGH_ARGUMENTS.getMessage(true));
                sender.sendMessage(Message.USAGE.getMessage(true).replace("%usage%", getUsage()));
                return;
            }
        } else {
            World world = Bukkit.getWorld(args[1]);
            if (world == null) {
                sender.sendMessage(Message.WORLD_NOT_FOUND.getMessage(true)
                        .replace("%world%", args[1]));
                return;
            }
            int x;
            try {x = Integer.parseInt(args[2]);}
            catch (IllegalArgumentException e) {
                sender.sendMessage(Message.NOT_VALID_INTEGER.getMessage(true)
                        .replace("%num%", args[2]));
                return;
            }
            int y;
            try {y = Integer.parseInt(args[3]);}
            catch (IllegalArgumentException e) {
                sender.sendMessage(Message.NOT_VALID_INTEGER.getMessage(true)
                        .replace("%num%", args[3]));
                return;
            }
            int z;
            try {z = Integer.parseInt(args[4]);}
            catch (IllegalArgumentException e) {
                sender.sendMessage(Message.NOT_VALID_INTEGER.getMessage(true)
                        .replace("%num%", args[4]));
                return;
            }
            location = new Location(world, x, y, z);
        }
        GiveLocationsManager giveLocationsManager = ((IGiveEvent) plugin).getGiveLocationsManager();
        GiveLocation giveLocation = giveLocationsManager.getLocation(name);
        if (giveLocation == null) {
            giveLocation = giveLocationsManager.add(name, location);
            sender.sendMessage(LocationUtils.formatMessage(Message.ADDED_LOCATION.getMessage(true),
                    "location", giveLocation.toBukkit()).replace("%name%", name));
        } else {
            sender.sendMessage(LocationUtils.formatMessage(
                    LocationUtils.formatMessage(Message.OVERWRITTEN_LOCATION.getMessage(true),
                            "location1", giveLocation.toBukkit()),
                    "location2", location).replace("%name%", name));
            giveLocation.setLocation(location);
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) list.add("<name>");
        if (args.length == 2) list.addAll(Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toList()));
        if (args.length > 2 && args.length < 6) {
            try {
                int num = Integer.parseInt(args[args.length - 1]);
                for (int i = 0; i < 10; i++) list.add(num + "" + i);
            } catch (IllegalArgumentException ignored) {}
        }
        return list;
    }

    @Override
    public int getMinArguments() {
        return 1;
    }
}