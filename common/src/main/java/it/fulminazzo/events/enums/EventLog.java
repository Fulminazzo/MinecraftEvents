package it.fulminazzo.events.enums;

import it.fulminazzo.events.utils.StringUtils;

public enum EventLog {
    INVALID_CLASS("Class %class% does not implement any interface %expected%. Please specify one before enabling the plugin."),

    VERSION_NOT_COMPATIBLE("&c%plugin% &fis not compatible with the current version. Please use &aMinecraft 1.%version% &for higher."),
    INCOMPATIBLE_VERSION("%feature% is not compatible with version %version%: every function will be disabled. " +
            "If you wish to use it, please update your Minecraft server."),

    PLUGIN_ENABLED("&c%plugin%&f(&bv%version%&f) by &6%author% &fsuccessfully &aenabled&f!"),
    ERROR_DURING_ENABLE("There was an error while enabling the plugin: %error%"),
    PLUGIN_DISABLED("&c%plugin%&f(&bv%version%&f) by &6%author% &fsuccessfully &cdisabled&f!"),
    ERROR_DURING_DISABLE("There was an error while disabling the plugin: %error%"),

    MESSAGE_PLUGIN_ERROR("&cPlugin has not been specified. Please contact the developers to let them know!"),
    MESSAGE_PATH_ERROR("&cPath has not been specified. Please contact the developers to let them know!"),
    MESSAGE_LANG_ERROR("&cLanguage file has not been found. Are you sure the plugin loaded correctly?"),
    MESSAGE_INTERNAL_ERROR("&cAn internal error occurred while getting message \"&4%path%&c\": &4%error%"),
    MESSAGE_PATH_NOT_FOUND("&cPath &4%path% &cnot found. Is there an error in your lang.yml?"),
    MESSAGE_ERROR("&4Message not found."),

    ENDER_DRAGON_ALREADY_PRESENT("An EnderDragon is already present in world %world%. Dragon respawn cancelled"),
    NOT_END("World %world% is not of type End"),

    MATERIAL_NOT_FOUND("Material \"%material%\" not found."),
    ;

    private final String message;

    EventLog(String message) {
        this.message = message;
    }

    public String getMessage(String... strings) {
        String m = message;
        if (strings != null && strings.length > 1)
            for (int i = 0; i < strings.length; i += 2)
                m = m.replace(strings[i], strings[i + 1]);
        return StringUtils.parseMessage(m);
    }
}