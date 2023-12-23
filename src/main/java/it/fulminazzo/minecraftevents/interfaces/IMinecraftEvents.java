package it.fulminazzo.minecraftevents.interfaces;

import it.fulminazzo.enderdragonevent.interfaces.IEnderDragonEvent;
import it.fulminazzo.events.enums.EventLog;
import it.fulminazzo.events.interfaces.IEventPlugin;
import it.fulminazzo.giveevent.interfaces.IGiveEvent;

public interface IMinecraftEvents extends IEventPlugin, IEnderDragonEvent, IGiveEvent {

    @Override
    default void loadAll() throws Exception {
        if (isVersion(IEnderDragonEvent.compatibleVersion)) IEnderDragonEvent.super.loadAll();
        else warning(EventLog.INCOMPATIBLE_VERSION, "%feature%", "EnderDragonEvent", "%version%", String.valueOf(getMinecraftVersion()));
        IGiveEvent.super.loadAll();
    }

    @Override
    default void unloadAll() throws Exception {
        if (isVersion(IEnderDragonEvent.compatibleVersion)) IEnderDragonEvent.super.unloadAll();
        IGiveEvent.super.unloadAll();
    }

    @Override
    default int getCompatibleVersion() {
        return -1;
    }
}
