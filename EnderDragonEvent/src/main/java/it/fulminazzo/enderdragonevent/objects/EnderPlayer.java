package it.fulminazzo.enderdragonevent.objects;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
@Setter
public class EnderPlayer {
    private final UUID uuid;
    private final String name;
    private double damageDealt;
    private double damageReceived;

    public EnderPlayer(Player player) {
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.damageDealt = 0;
        this.damageReceived = 0;
    }

    public void increaseDamageDealt(double damage) {
        setDamageDealt(getDamageDealt() + damage);
    }

    public void increaseDamageReceived(double damage) {
        setDamageReceived(getDamageReceived() + damage);
    }

    public boolean equals(Player player) {
        return player != null && player.getUniqueId().equals(uuid);
    }
}