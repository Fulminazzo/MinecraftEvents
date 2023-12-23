package it.fulminazzo.giveevent.utils.PluginsUtils.ItemsUtils;

import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomStack;
import it.fulminazzo.giveevent.interfaces.ItemPluginUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;

public class ItemsAdderUtils implements ItemPluginUtil {

    @Override
    public String getNameFromItemStack(ItemStack itemStack) {
        CustomStack stack = ItemsAdderUtils.getCustomStackFromItemStack(itemStack);
        if (stack == null) return null;
        try {
            Method method = stack.getClass().getMethod("getNamespacedID");
            return (String) method.invoke(stack);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ItemStack getItemStackFromBlock(Block block) {
        CustomBlock customBlock = CustomBlock.byAlreadyPlaced(block);
        if (customBlock != null) return customBlock.getItemStack();
        else return null;
    }

    @Override
    public ItemStack getItemStackFromItemStack(ItemStack itemStack) {
        if (itemStack == null) return null;
        CustomStack customStack = CustomStack.byItemStack(itemStack);
        if (customStack != null) return customStack.getItemStack();
        else return null;
    }

    @Override
    public ItemStack getItemStackFromName(String name) {
        if (name == null) return null;
        CustomStack customStack = CustomStack.getInstance(name);
        if (customStack != null) return customStack.getItemStack();
        else return null;
    }

    @Override
    public boolean isBlock(ItemStack itemStack) {
        CustomStack stack = getCustomStackFromItemStack(itemStack);
        return stack != null && stack.isBlock();
    }

    @Override
    public boolean setCustomBlock(Block block, ItemStack itemStack) {
        return setCustomBlock(block == null ? null : block.getLocation(), itemStack);
    }

    @Override
    public boolean setCustomBlock(Location location, ItemStack itemStack) {
        if (location == null) return false;
        CustomStack customStack = getCustomStackFromItemStack(itemStack);
        if (customStack == null) return false;
        CustomBlock.place(customStack.getNamespacedID(), location);
        return true;
    }

    public static CustomStack getCustomStackFromItemStack(ItemStack itemStack) {
        return CustomStack.byItemStack(itemStack);
    }
}