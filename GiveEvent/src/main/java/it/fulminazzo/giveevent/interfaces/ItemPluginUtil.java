package it.fulminazzo.giveevent.interfaces;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public interface ItemPluginUtil extends PluginUtil {

    String getNameFromItemStack(ItemStack itemStack);

    default String getNameFromBlock(Block block) {
        if (block == null) return null;
        ItemStack itemStack = getItemStackFromBlock(block);
        if (itemStack == null) return null;
        return getNameFromItemStack(itemStack);
    }

    ItemStack getItemStackFromBlock(Block block);

    ItemStack getItemStackFromItemStack(ItemStack itemStack);

    ItemStack getItemStackFromName(String name);

    boolean isBlock(ItemStack itemStack);

    boolean setCustomBlock(Block block, ItemStack itemStack);

    boolean setCustomBlock(Location location, ItemStack itemStack);

    default void setCustomBlock(Block block, String itemName) {
        if (block == null || itemName == null) return;
        ItemStack itemStack = getItemStackFromName(itemName);
        if (itemStack != null) setCustomBlock(block, itemStack);
    }
}