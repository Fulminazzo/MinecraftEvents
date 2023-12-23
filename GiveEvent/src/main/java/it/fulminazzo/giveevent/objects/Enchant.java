package it.fulminazzo.giveevent.objects;

import lombok.Getter;
import org.bukkit.enchantments.Enchantment;

@Getter
public class Enchant {
    private Enchantment enchantment;
    private int level;

    public Enchant() {

    }

    public Enchant(Enchantment enchantment, int level) {
        this.enchantment = enchantment;
        this.level = level;
    }
}