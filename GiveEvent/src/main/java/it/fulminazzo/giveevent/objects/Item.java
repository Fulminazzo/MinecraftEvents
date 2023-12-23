package it.fulminazzo.giveevent.objects;

import it.fulminazzo.events.enums.EventLog;
import it.fulminazzo.events.interfaces.IEventPlugin;
import it.fulminazzo.events.utils.VersionsUtils;
import it.fulminazzo.fulmicollection.objects.Printable;
import it.fulminazzo.giveevent.utils.PluginUtils;
import it.fulminazzo.yamlparser.annotations.PreventSaving;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class Item extends Printable {
    @PreventSaving
    private final IEventPlugin plugin;
    private String materialName;
    private Integer amount;
    private String name;
    private List<String> lore;
    private List<Enchant> enchants;
    private List<ItemFlag> itemFlags;
    private Integer customModelData;

    public Item(IEventPlugin plugin) {
        this.plugin = plugin;
        this.lore = new ArrayList<>();
        this.enchants = new ArrayList<>();
        this.itemFlags = new ArrayList<>();
    }

    public Item(IEventPlugin plugin, ItemStack itemStack) {
        this(plugin);
        if (itemStack == null) return;
        materialName = new PluginUtils().getNameFromItemStack(itemStack);
        this.amount = itemStack.getAmount();
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return;
        this.name = meta.getDisplayName();
        List<String> lore = meta.getLore();
        if (lore != null) this.lore.addAll(lore);
        this.enchants = meta.getEnchants().entrySet().stream()
                .map(e -> new Enchant(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
        this.itemFlags = new ArrayList<>(meta.getItemFlags());
        if (VersionsUtils.is1_(14)) {
            if (meta.hasCustomModelData()) this.customModelData = meta.getCustomModelData();
        }
    }

    public ItemStack toBukkit() {
        ItemStack itemStack = new PluginUtils().getItemStackFromName(materialName);
        if (itemStack == null) {
            plugin.warning(EventLog.MATERIAL_NOT_FOUND, "%material%", materialName);
            return null;
        }
        itemStack.setAmount(Math.max(1, amount));
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            if (name != null) meta.setDisplayName(name);
            if (lore != null) meta.setLore(lore);
            if (enchants != null) enchants.forEach(e -> meta.addEnchant(e.getEnchantment(), e.getLevel(), true));
            if (itemFlags != null) meta.addItemFlags(itemFlags.toArray(new ItemFlag[0]));
            if (VersionsUtils.is1_(14)) {
                if (customModelData != null) meta.setCustomModelData(customModelData);
            }
            itemStack.setItemMeta(meta);
        }
        return itemStack;
    }

    public boolean equals(ItemStack itemStack) {
        if (itemStack == null) return false;
        itemStack.setAmount(1);
        ItemStack item = toBukkit();
        if (item == null) return false;
        item.setAmount(1);
        return item.equals(itemStack);
    }
}