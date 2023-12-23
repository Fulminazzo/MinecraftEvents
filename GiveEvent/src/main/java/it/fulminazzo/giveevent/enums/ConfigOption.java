package it.fulminazzo.giveevent.enums;

import it.fulminazzo.events.interfaces.IConfigOption;
import lombok.Getter;

@Getter
public enum ConfigOption implements IConfigOption {
    DROP_STRATEGY("give-event.drop-strategy"),
    DROP_INTERVAL("give-event.drop-interval"),
    GIVE_EVENT_TIMER("give-event.timer"),
    MESSAGES("give-event.messages")
    ;

    private final String path;

    ConfigOption(String path) {
        this.path = path;
    }

    public GiveStrategy getGiveStrategy() {
        return GiveStrategy.valueOf(getString().toUpperCase());
    }
}
