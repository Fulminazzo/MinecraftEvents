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

public class ListGiveLocations extends EventSubCommand {

    public ListGiveLocations(IEventPlugin plugin, EventCommand command) {
        super(plugin, command, "listgivelocations", GEPermission.LIST_GIVE_LOCATIONS,
                Message.LIST_GIVE_LOCATIONS_DESCRIPTION.getMessage(false),
                "listgivelocations", "listlocations", "listl");
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        GiveLocationsManager giveLocationsManager = ((IGiveEvent) plugin).getGiveLocationsManager();
        List<GiveLocation> locations = giveLocationsManager.getLocations();
        if (locations.isEmpty()) sender.sendMessage(Message.NO_LOCATION_SET.getMessage(true));
        else for (int i = 0; i < locations.size(); i++)
            sender.sendMessage(LocationUtils.formatMessage(Message.LIST_ELEMENT.getMessage(false)
                            .replace("%index%", String.valueOf(i + 1)),
                    "element", locations.get(i).toBukkit()));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public int getMinArguments() {
        return 0;
    }
}