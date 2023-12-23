package it.fulminazzo.giveevent.managers;

import it.fulminazzo.events.interfaces.IEventPlugin;
import it.fulminazzo.giveevent.objects.Item;
import it.fulminazzo.yamlparser.objects.configurations.FileConfiguration;
import it.fulminazzo.yamlparser.utils.FileUtils;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ItemsManager {
    private final IEventPlugin plugin;
    private final File itemsFile;
    @Getter
    private final List<Item> items;

    public ItemsManager(IEventPlugin plugin) {
        this.plugin = plugin;
        this.itemsFile = new File(plugin.getDataFolder(), "items.yml");
        this.items = new ArrayList<>();
    }

    public void reloadAll() {
        if (!this.itemsFile.isFile()) return;
        FileConfiguration configuration = new FileConfiguration(itemsFile);
        this.items.addAll(configuration.getList("items", Item.class));
    }

    public Item add(ItemStack itemStack) {
        Item item = new Item(plugin, itemStack);
        this.items.add(item);
        return item;
    }

    public void remove(Item item) {
        if (item != null) this.items.removeIf(r -> r.equals(item));
    }

    public void saveAll() throws IOException {
        if (!this.itemsFile.isFile()) FileUtils.createNewFile(this.itemsFile);
        FileConfiguration configuration = new FileConfiguration(itemsFile);
        configuration.set("items", items);
        configuration.save();
    }
}