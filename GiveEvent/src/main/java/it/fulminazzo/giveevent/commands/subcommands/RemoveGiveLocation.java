package it.fulminazzo.giveevent.commands.subcommands;

import it.fulminazzo.events.commands.EventCommand;
import it.fulminazzo.events.commands.subcommands.EventSubCommand;
import it.fulminazzo.events.interfaces.IEventPlugin;
import it.fulminazzo.giveevent.enums.GEPermission;
import it.fulminazzo.giveevent.enums.Message;
import it.fulminazzo.giveevent.interfaces.IGiveEvent;
import it.fulminazzo.giveevent.managers.GiveLocationsManager;
import it.fulminazzo.giveevent.objects.GiveLocation;
import it.fulminazzo.giveevent.utils.LocationUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RemoveGiveLocation extends EventSubCommand {

    public RemoveGiveLocation(IEventPlugin plugin, EventCommand command) {
        super(plugin, command, "removegivelocation", GEPermission.REMOVE_GIVE_LOCATION,
                Message.REMOVE_GIVE_LOCATION_DESCRIPTION.getMessage(false),
                "removegivelocation <index>", "removelocation", "removel");
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        GiveLocationsManager giveLocationsManager = ((IGiveEvent) plugin).getGiveLocationsManager();
        List<GiveLocation> locations = giveLocationsManager.getLocations();
        if (locations.isEmpty()) sender.sendMessage(Message.NO_LOCATION_SET.getMessage(true));
        else {
            int n = 0;
            try {
                n = Integer.parseInt(args[0]);
                if (n < 1) throw new IllegalArgumentException();
                GiveLocation location = locations.get(n - 1);
                giveLocationsManager.remove(location);
                sender.sendMessage(LocationUtils.formatMessage(Message.REMOVED_LOCATION.getMessage(true)
                        .replace("%name%", location.getName()), "location", location.toBukkit()));
            } catch (IllegalArgumentException e) {
                sender.sendMessage(Message.NOT_VALID_NUMBER.getMessage(true)
                        .replace("%num%", args[0]));
            } catch (ArrayIndexOutOfBoundsException e) {
                sender.sendMessage(Message.NOT_IN_LIST.getMessage(true)
                        .replace("%num%", String.valueOf(n))
                        .replace("%max%", String.valueOf(locations.size())));
            }
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        GiveLocationsManager giveLocationsManager = ((IGiveEvent) plugin).getGiveLocationsManager();
        List<GiveLocation> locations = giveLocationsManager.getLocations();
        List<String> list = new ArrayList<>();
        if (args.length == 1)
            list.addAll(IntStream.range(0, locations.size())
                    .map(i -> i + 1)
                    .mapToObj(String::valueOf)
                    .collect(Collectors.toList()));
        return list;
    }

    @Override
    public int getMinArguments() {
        return 1;
    }
}