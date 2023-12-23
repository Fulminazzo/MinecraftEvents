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
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;

public class SetItem extends EventSubCommand {

    public SetItem(IEventPlugin plugin, EventCommand command) {
        super(plugin, command, "setitem", GEPermission.SET_ITEM,
                Message.SET_ITEM_DESCRIPTION.getMessage(false),
                "setitem");
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player))
            sender.sendMessage(Message.CONSOLE_CANNOT_EXECUTE.getMessage(true));
        else {
            PlayerInventory playerInventory = ((Player) sender).getInventory();
            ItemStack itemStack = playerInventory.getItem(playerInventory.getHeldItemSlot());
            if (itemStack == null || itemStack.getType().equals(Material.AIR))
                sender.sendMessage(Message.NOT_HOLDING_ITEM.getMessage(true));
            else {
                ItemsManager itemsManager = ((IGiveEvent) plugin).getItemsManager();
                if (itemsManager.getItems().stream().anyMatch(i -> i.equals(itemStack)))
                    sender.sendMessage(Message.ITEM_ALREADY_PRESENT.getMessage(true));
                else {
                    Item item = itemsManager.add(itemStack);
                    sender.sendMessage(Message.ADDED_ITEM.getMessage(true)
                            .replace("%item%", item.getMaterialName()));
                }
            }
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
