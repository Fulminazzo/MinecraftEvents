package it.fulminazzo.giveevent.commands.subcommands;

import it.fulminazzo.events.EventPlugin;
import it.fulminazzo.events.commands.EventCommand;
import it.fulminazzo.events.commands.subcommands.EventSubCommand;
import it.fulminazzo.events.interfaces.IEventPlugin;
import it.fulminazzo.giveevent.enums.GEPermission;
import it.fulminazzo.giveevent.enums.Message;
import it.fulminazzo.giveevent.interfaces.IGiveEvent;
import it.fulminazzo.giveevent.managers.ItemsManager;
import it.fulminazzo.giveevent.objects.Item;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class ListItems extends EventSubCommand {

    public ListItems(IEventPlugin plugin, EventCommand command) {
        super(plugin, command, "listitems", GEPermission.LIST_ITEMS,
                Message.LIST_ITEMS_DESCRIPTION.getMessage(false),
                "listitems", "listi");
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        ItemsManager itemsManager = ((IGiveEvent) plugin).getItemsManager();
        List<Item> items = itemsManager.getItems();
        if (items.isEmpty()) sender.sendMessage(Message.NO_ITEM_SET.getMessage(true));
        else for (int i = 0; i < items.size(); i++)
            sender.sendMessage(Message.LIST_ELEMENT.getMessage(false)
                    .replace("%index%", String.valueOf(i + 1))
                    .replace("%element%", items.get(i).getMaterialName()));
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