package nl.thieme.tp.events.custom;

import nl.thieme.tp.models.Present;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;

public class PresentInitEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    List<Present> presentList;

    public PresentInitEvent(List<Present> presentList) {
        this.presentList = presentList;
    }

    public List<Present> getPresents() {
        return presentList;
    }


    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
