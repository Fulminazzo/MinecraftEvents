package it.fulminazzo.enderdragonevent.commands;

import it.fulminazzo.enderdragonevent.commands.subcommands.Start;
import it.fulminazzo.enderdragonevent.commands.subcommands.Stop;
import it.fulminazzo.enderdragonevent.enums.Message;
import it.fulminazzo.events.commands.EventSubCommandable;
import it.fulminazzo.events.interfaces.IEventPlugin;

public class EnderDragonEventCommand extends EventSubCommandable {

    public EnderDragonEventCommand(IEventPlugin plugin) {
        super(plugin, "enderdragonevent");
        addSubCommands(new Start(plugin, this), new Stop(plugin, this));
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
