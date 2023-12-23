package it.fulminazzo.giveevent.enums;

import it.fulminazzo.events.interfaces.IMessage;
import lombok.Getter;

@Getter
public enum Message implements IMessage {
    PREFIX("prefix"),

    CONSOLE_CANNOT_EXECUTE("errors.console-cannot-execute"),
    SUBCOMMAND_NOT_FOUND("errors.subcommand-not-found"),
    NOT_ENOUGH_PERMISSIONS("errors.not-enough-permissions"),
    NOT_ENOUGH_ARGUMENTS("errors.not-enough-arguments"),
    WORLD_NOT_FOUND("errors.world-not-found"),
    NOT_VALID_INTEGER("errors.not-valid-integer"),
    NOT_VALID_NUMBER("errors.not-valid-number"),
    GIVE_EVENT_ALREADY_RUNNING("errors.give-event-already-running"),
    GIVE_EVENT_NOT_RUNNING("errors.give-event-not-running"),
    NO_ITEM_SET("errors.no-item-set"),
    NO_LOCATION_SET("errors.no-location-set"),
    NOT_IN_LIST("errors.not-in-list"),
    NOT_HOLDING_ITEM("errors.not-holding-item"),
    ITEM_ALREADY_PRESENT("errors.item-already-present"),

    USAGE("general.usage"),
    ADDED_LOCATION("general.added-location"),
    OVERWRITTEN_LOCATION("general.overwritten-location"),
    LOCATION_FORMAT("general.location-format"),
    GIVE_EVENT_STARTED("general.give-event-started"),
    GIVE_EVENT_STOPPED("general.give-event-stopped"),
    LIST_ELEMENT("general.list-element"),
    REMOVED_LOCATION("general.removed-location"),
    ADDED_ITEM("general.added-item"),
    REMOVED_ITEM("general.removed-item"),

    START_GIVE_EVENT_DESCRIPTION("help.start-give-event"),
    STOP_GIVE_EVENT_DESCRIPTION("help.stop-give-event"),
    SET_GIVE_LOCATION_DESCRIPTION("help.set-give-location"),
    LIST_GIVE_LOCATIONS_DESCRIPTION("help.list-give-locations"),
    REMOVE_GIVE_LOCATION_DESCRIPTION("help.remove-give-location"),
    SET_ITEM_DESCRIPTION("help.set-item"),
    LIST_ITEMS_DESCRIPTION("help.list-items"),
    REMOVE_ITEM_DESCRIPTION("help.remove-item"),
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
