package it.fulminazzo.enderdragonevent.enums;

import it.fulminazzo.events.interfaces.IMessage;
import lombok.Getter;

@Getter
public enum Message implements IMessage {
    PREFIX("prefix"),

    SUBCOMMAND_NOT_FOUND("errors.subcommand-not-found"),
    NOT_ENOUGH_PERMISSIONS("errors.not-enough-permissions"),
    NOT_ENOUGH_ARGUMENTS("errors.not-enough-arguments"),
    NOT_VALID_NUMBER("errors.not-valid-number"),
    NOT_VALID_ENUM("errors.not-valid-enum"),
    WORLD_NOT_FOUND("errors.world-not-found"),
    WORLD_NOT_END("errors.world-not-end"),
    ENDER_DRAGON_STILL_ALIVE("errors.ender-dragon-still-alive"),
    ENDER_DRAGON_EVENT_ALREADY_RUNNING("errors.ender-dragon-event-already-running"),
    ENDER_DRAGON_EVENT_NOT_RUNNING("errors.ender-dragon-event-not-running"),

    USAGE("general.usage"),
    SPAWNING_ENDER_DRAGON("general.spawning-ender-dragon"),
    ENDER_DRAGON_EVENT_STOPPED("general.ender-dragon-event-stopped"),

    START_DRAGON_EVENT_DESCRIPTION("help.start-dragon-event"),
    STOP_DRAGON_EVENT_DESCRIPTION("help.stop-dragon-event"),
    ;

    private final String path;

    Message(String path) {
        this.path = path;
    }

    @Override
    public IMessage getPrefix() {
        return PREFIX;
    }
}