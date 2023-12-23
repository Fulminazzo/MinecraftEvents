package it.fulminazzo.enderdragonevent.commands.subcommands;

import it.fulminazzo.enderdragonevent.enums.EDPermission;
import it.fulminazzo.enderdragonevent.enums.Message;
import it.fulminazzo.enderdragonevent.interfaces.IEnderDragonEvent;
import it.fulminazzo.enderdragonevent.managers.EnderDragonManager;
import it.fulminazzo.enderdragonevent.services.EnderDragonService;
import it.fulminazzo.events.commands.EventCommand;
import it.fulminazzo.events.commands.subcommands.EventSubCommand;
import it.fulminazzo.events.interfaces.IEventPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Stop extends EventSubCommand {

    public Stop(IEventPlugin plugin, EventCommand command) {
        super(plugin, command, "stop", EDPermission.STOP,
                Message.STOP_DRAGON_EVENT_DESCRIPTION.getMessage(true),
                "stop <world>");
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        World world;
        EnderDragonService enderDragonService = ((IEnderDragonEvent) plugin).getEnderDragonService();
        if (!enderDragonService.isTimerActive()) {
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
            EnderDragonManager enderDragonManager = enderDragonService.getEnderDragonManager(world);
            if (enderDragonManager.isIdle()) {
                sender.sendMessage(Message.ENDER_DRAGON_EVENT_NOT_RUNNING.getMessage(true).replace("%world%", world.getName()));
                return;
            }
            enderDragonManager.stopAll();
        }
        enderDragonService.stopTimer();
        sender.sendMessage(Message.ENDER_DRAGON_EVENT_STOPPED.getMessage(true));
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
