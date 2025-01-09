package org.jhotdraw.gui.fontchooser;

import javax.swing.event.EventListenerList;
import java.util.EventListener;

public class ListenerManagementSupport {
    private EventListenerList listenerList;

    public ListenerManagementSupport(EventListenerList listenerList) {
        this.listenerList = listenerList;
    }

    public <T extends EventListener> T[] getListeners(Class<T> listenerType) {
        return listenerList.getListeners(listenerType);
    }
}