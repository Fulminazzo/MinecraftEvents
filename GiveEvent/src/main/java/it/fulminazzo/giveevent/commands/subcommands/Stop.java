package it.fulminazzo.giveevent.commands.subcommands;

import it.fulminazzo.events.EventPlugin;
import it.fulminazzo.events.commands.EventCommand;
import it.fulminazzo.events.commands.subcommands.EventSubCommand;
import it.fulminazzo.events.interfaces.IEventPlugin;
import it.fulminazzo.giveevent.enums.GEPermission;
import it.fulminazzo.giveevent.enums.Message;
import it.fulminazzo.giveevent.interfaces.IGiveEvent;
import it.fulminazzo.giveevent.managers.GiveEventManager;
import it.fulminazzo.reflectionutils.objects.ReflObject;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class Stop extends EventSubCommand {

    public Stop(IEventPlugin plugin, EventCommand command) {
        super(plugin, command, "stop", GEPermission.STOP,
                Message.STOP_GIVE_EVENT_DESCRIPTION.getMessage(false),
                "stop");
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        GiveEventManager giveEventManager = ((IGiveEvent) plugin).getGiveEventManager();
        if (!giveEventManager.isRunning())
            sender.sendMessage(Message.GIVE_EVENT_NOT_RUNNING.getMessage(true));
        else {
            giveEventManager.stopEvent();
            sender.sendMessage(Message.GIVE_EVENT_STOPPED.getMessage(true));
        }
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