package it.fulminazzo.events.interfaces;

import it.fulminazzo.events.EventPlugin;
import it.fulminazzo.events.enums.EventLog;
import it.fulminazzo.events.utils.StringUtils;
import it.fulminazzo.yamlparser.objects.configurations.FileConfiguration;

public interface IMessage {

    default String getMessage(boolean prefix) {
        String message;
        IEventPlugin plugin = EventPlugin.getPlugin();
        String path = getPath();
        if (path == null) message = EventLog.MESSAGE_PATH_ERROR.getMessage();
        else {
            FileConfiguration langConfig = plugin.getLang();
            if (langConfig == null) message = EventLog.MESSAGE_LANG_ERROR.getMessage();
            else {
                try {
                    message = langConfig.getString(path);
                } catch (Exception e) {
                    message = EventLog.MESSAGE_INTERNAL_ERROR.getMessage(
                            "%path%", path, "%error%", e.getMessage());
                }
                if (message == null) message = EventLog.MESSAGE_PATH_NOT_FOUND.getMessage("%path%", path);
                else if (prefix) message = getPrefix().getMessage(false) + message;
            }
        }
        return StringUtils.parseMessage(message);
    }

    String getPath();

    IMessage getPrefix();
}
