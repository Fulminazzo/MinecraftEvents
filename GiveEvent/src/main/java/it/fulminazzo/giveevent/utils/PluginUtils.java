package it.fulminazzo.giveevent.utils;

import it.fulminazzo.events.EventPlugin;
import it.fulminazzo.giveevent.interfaces.ItemPluginUtil;
import it.fulminazzo.giveevent.utils.PluginsUtils.ItemsUtils.ItemStackUtils;
import it.fulminazzo.giveevent.utils.PluginsUtils.ItemsUtils.ItemsAdderUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;

public class PluginUtils implements ItemPluginUtil {

    @Override
    public String getNameFromItemStack(ItemStack itemStack) {
        if (itemStack == null) return null;
        return executeItemFunction("getNameFromItemStack", (o, i) -> o != null && !o.trim().isEmpty(), itemStack);
    }

    @Override
    public ItemStack getItemStackFromBlock(Block block) {
        if (block == null) return null;
        return executeItemFunction("getItemStackFromBlock", (o, i) -> o != null, block);
    }

    @Override
    public ItemStack getItemStackFromItemStack(ItemStack itemStack) {
        return executeItemFunction("getItemStackFromItemStack", (o, i) -> o != null, itemStack);
    }

    @Override
    public ItemStack getItemStackFromName(String name) {
        return executeItemFunction("getItemStackFromName", (o, i) -> o != null, name);
    }

    @Override
    public boolean isBlock(ItemStack itemStack) {
        Object result = executeItemFunction("isBlock", (o, i) -> (Boolean) o, itemStack);
        return result instanceof Boolean && (boolean) result;
    }

    @Override
    public boolean setCustomBlock(Block block, ItemStack itemStack) {
        if (block != null && itemStack != null && itemStack.getType().equals(Material.AIR)) {
            block.setType(Material.AIR);
            return true;
        }
        Object result = executeItemFunction("setCustomBlock", (o, i) -> (Boolean) o, block, itemStack);
        return result instanceof Boolean && (boolean) result;
    }

    @Override
    public boolean setCustomBlock(Location location, ItemStack itemStack) {
        if (location != null && itemStack != null && itemStack.getType().equals(Material.AIR)) {
            location.getBlock().setType(Material.AIR);
            return true;
        }
        Object result = executeItemFunction("setCustomBlock", (o, i) -> (Boolean) o, location, itemStack);
        return result instanceof Boolean && (boolean) result;
    }

    @Override
    public void setCustomBlock(Block block, String itemName) {
        if (block == null || itemName == null) return;
        ItemStack itemStack = executeItemFunction("getItemStackFromName", (o, i) -> o != null && isBlock(o), itemName);
        if (itemStack != null && !setCustomBlock(block, itemStack)) {
            Location location = block.getLocation();
            World world = location.getWorld();
            EventPlugin.getPlugin().getLogger().warning(String.format("Could not set block %s at Location(world: %s, x: %s, y: %s, z: %s)",
                    itemName, world == null ? null : world.getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        }
    }

    @SuppressWarnings({"unchecked"})
    public <O> O executeItemFunction(String functionName, BiFunction<O, ItemPluginUtil, Boolean> validator, Object... args) {
        LinkedList<Map.Entry<Callable<Boolean>, ItemPluginUtil>> list = new LinkedList<>();
        list.add(new AbstractMap.SimpleEntry<>(PluginUtils::isItemsAdderEnabled, new ItemsAdderUtils()));
        list.add(new AbstractMap.SimpleEntry<>(() -> true, new ItemStackUtils()));
        for (Map.Entry<Callable<Boolean>, ItemPluginUtil> entry : list) {
            try {
                if (!entry.getKey().call()) continue;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            ItemPluginUtil util = entry.getValue();
            Object object = null;
            Method method = util.getMethod(functionName, args);
            if (!method.getReturnType().equals(Void.TYPE)) object = util.invokeMethod(functionName, args);
            else util.invokeMethod(functionName, args);
            if (validator != null && validator.apply((O) object, util))
                return (O) object;
        }
        return null;
    }

    public static boolean isItemsAdderEnabled() {
        return isPluginEnabled("ItemsAdder");
    }

    private static boolean isPluginEnabled(String pluginName) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
        return plugin != null && plugin.isEnabled();
    }
}
