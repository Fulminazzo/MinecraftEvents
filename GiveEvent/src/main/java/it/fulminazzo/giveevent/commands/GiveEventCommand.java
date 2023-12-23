package it.fulminazzo.giveevent.commands;

import it.fulminazzo.events.commands.EventSubCommandable;
import it.fulminazzo.events.interfaces.IEventPlugin;
import it.fulminazzo.giveevent.commands.subcommands.*;
import it.fulminazzo.giveevent.enums.Message;

public class GiveEventCommand extends EventSubCommandable {

    public GiveEventCommand(IEventPlugin plugin) {
        super(plugin, "giveevent");
        addSubCommands(
                new SetGiveLocation(plugin, this),
                new ListGiveLocations(plugin, this),
                new RemoveGiveLocation(plugin, this),
                new SetItem(plugin, this),
                new ListItems(plugin, this),
                new RemoveItem(plugin, this),
                new Start(plugin, this),
                new Stop(plugin, this)
        );
    }

    @Override
    public String subCommandNotFound() {
        return Message.SUBCOMMAND_NOT_FOUND.getMessage(true);
    }

    @Override
    public String notEnoughArguments() {
        return Message.NOT_ENOUGH_ARGUMENTS.getMessage(true);
    }

    @Override
    public String notEnoughPermissions() {
        return Message.NOT_ENOUGH_PERMISSIONS.getMessage(true);
    }

    @Override
    public String usage() {
        return Message.USAGE.getMessage(true);
    }
}