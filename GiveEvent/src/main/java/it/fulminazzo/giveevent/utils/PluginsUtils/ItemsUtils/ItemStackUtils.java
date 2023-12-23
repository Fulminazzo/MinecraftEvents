package it.fulminazzo.giveevent.utils.PluginsUtils.ItemsUtils;

import it.fulminazzo.giveevent.interfaces.ItemPluginUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class ItemStackUtils implements ItemPluginUtil {

    @Override
    public String getNameFromItemStack(ItemStack itemStack) {
        return itemStack == null ? null : itemStack.getType().name();
    }

    @Override
    public ItemStack getItemStackFromBlock(Block block) {
        if (block == null) return null;
        return new ItemStack(block.getType());
    }

    @Override
    public ItemStack getItemStackFromItemStack(ItemStack itemStack) {
        return itemStack;
    }

    @Override
    public ItemStack getItemStackFromName(String name) {
        if (name == null) return null;
        Material material = Arrays.stream(Material.values())
                .filter(m -> m.name().equalsIgnoreCase(name))
                .findFirst().orElse(null);
        return material == null ? null : new ItemStack(material);
    }

    @Override
    public boolean isBlock(ItemStack itemStack) {
        return itemStack != null && itemStack.getType().isBlock();
    }

    @Override
    public boolean setCustomBlock(Block block, ItemStack itemStack) {
        if (block != null && itemStack != null) {
            block.setType(itemStack.getType());
            return true;
        }
        return false;
    }

    @Override
    public boolean setCustomBlock(Location location, ItemStack itemStack) {
        return setCustomBlock(location == null ? null : location.getBlock(), itemStack);
    }
}
