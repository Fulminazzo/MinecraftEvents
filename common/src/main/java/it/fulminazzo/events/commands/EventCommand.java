package it.fulminazzo.events.commands;

import it.fulminazzo.events.EventPlugin;
import it.fulminazzo.events.interfaces.IEventPlugin;
import lombok.Getter;

@Getter
public abstract class EventCommand {
    protected final IEventPlugin eventPlugin;
    private final String name;

    public EventCommand(IEventPlugin eventPlugin, String name) {
        this.eventPlugin = eventPlugin;
        this.name = name;
    }
}
