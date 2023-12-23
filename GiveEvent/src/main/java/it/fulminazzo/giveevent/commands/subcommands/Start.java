package it.fulminazzo.giveevent.commands.subcommands;

import it.fulminazzo.events.EventPlugin;
import it.fulminazzo.events.commands.EventCommand;
import it.fulminazzo.events.commands.subcommands.EventSubCommand;
import it.fulminazzo.events.interfaces.IEventPlugin;
import it.fulminazzo.giveevent.enums.ConfigOption;
import it.fulminazzo.giveevent.enums.GEPermission;
import it.fulminazzo.giveevent.enums.Message;
import it.fulminazzo.giveevent.interfaces.IGiveEvent;
import it.fulminazzo.giveevent.managers.GiveEventManager;
import it.fulminazzo.giveevent.managers.GiveLocationsManager;
import it.fulminazzo.giveevent.managers.ItemsManager;
import it.fulminazzo.reflectionutils.objects.ReflObject;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class Start extends EventSubCommand {

    public Start(IEventPlugin plugin, EventCommand command) {
        super(plugin, command, "start", GEPermission.START,
                Message.START_GIVE_EVENT_DESCRIPTION.getMessage(false),
                "start");
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        ItemsManager itemsManager = ((IGiveEvent) plugin).getItemsManager();
        if (itemsManager == null || itemsManager.getItems().isEmpty()) {
            sender.sendMessage(Message.NO_ITEM_SET.getMessage(true));
            return;
        }
        GiveLocationsManager locationsManager = ((IGiveEvent) plugin).getGiveLocationsManager();
        if (locationsManager == null || locationsManager.getLocations().isEmpty()) {
            sender.sendMessage(Message.NO_LOCATION_SET.getMessage(true));
            return;
        }
        GiveEventManager giveEventManager = ((IGiveEvent) plugin).getGiveEventManager();
        if (giveEventManager.isRunning())
            sender.sendMessage(Message.GIVE_EVENT_ALREADY_RUNNING.getMessage(true));
        else {
            int time = ConfigOption.GIVE_EVENT_TIMER.getInteger();
            if (args.length > 0) {
                try {
                    time = Integer.parseInt(args[0]);
                    if (time <= 0) throw new IllegalArgumentException();
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(Message.NOT_VALID_NUMBER.getMessage(true)
                            .replace("%num%", args[0]));
                    return;
                }
            }
            giveEventManager.startEvent(time);
            sender.sendMessage(Message.GIVE_EVENT_STARTED.getMessage(true)
                    .replace("%time%", String.valueOf(time)));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) list.add("<time>");
        return list;
    }

    @Override
    public int getMinArguments() {
        return 0;
    }
}