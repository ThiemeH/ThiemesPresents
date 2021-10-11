package nl.thieme.tp.events.custom;


import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PresentSignEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final ItemStack presentStack;
    private boolean isCancelled;

    public PresentSignEvent(Player player, ItemStack presentStack) {
        this.player = player;
        this.presentStack = presentStack;
    }

    public ItemStack getPresentItemStack() {
        return presentStack;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        isCancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public static class Pre extends PresentSignEvent {

        public Pre(Player p, ItemStack is) {
            super(p, is);
        }
    }

    public static class Post extends PresentSignEvent {

        private final String msg;

        public Post(Player player, ItemStack is, String msg) {
            super(player, is);
            this.msg = msg;
        }

        public String getMessage() {
            return msg;
        }
    }
}
