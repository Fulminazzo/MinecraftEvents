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
import it.fulminazzo.reflectionutils.objects.ReflObject;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RemoveItem extends EventSubCommand {

    public RemoveItem(IEventPlugin plugin, EventCommand command) {
        super(plugin, command, "removeitem", GEPermission.REMOVE_ITEM,
                Message.REMOVE_ITEM_DESCRIPTION.getMessage(false),
                "removeitem <index>", "removei");
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        ItemsManager itemsManager = ((IGiveEvent) plugin).getItemsManager();
        List<Item> items = itemsManager.getItems();
        if (items.isEmpty()) sender.sendMessage(Message.NO_ITEM_SET.getMessage(true));
        else {
            int n = 0;
            try {
                n = Integer.parseInt(args[0]);
                if (n < 1) throw new IllegalArgumentException();
                Item item = items.get(n - 1);
                itemsManager.remove(item);
                sender.sendMessage(Message.REMOVED_ITEM.getMessage(true)
                        .replace("%item%", item.getMaterialName()));
            } catch (IllegalArgumentException e) {
                sender.sendMessage(Message.NOT_VALID_NUMBER.getMessage(true)
                        .replace("%num%", args[0]));
            } catch (ArrayIndexOutOfBoundsException e) {
                sender.sendMessage(Message.NOT_IN_LIST.getMessage(true)
                        .replace("%num%", String.valueOf(n))
                        .replace("%max%", String.valueOf(items.size())));
            }
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        ItemsManager itemsManager = ((IGiveEvent) plugin).getItemsManager();
        List<Item> items = itemsManager.getItems();
        List<String> list = new ArrayList<>();
        if (args.length == 1)
            list.addAll(IntStream.range(0, items.size())
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