package it.fulminazzo.enderdragonevent.exceptions;

import it.fulminazzo.events.enums.EventLog;
import org.bukkit.World;

public class NotEndException extends RuntimeException {

    public NotEndException(World world) {
        super(EventLog.NOT_END.getMessage("%world%", world.getName()));
    }
}
