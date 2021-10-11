package nl.thieme.tp.events.custom;


import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PresentSignEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final ItemStack presentStack;

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
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static class Pre extends PresentSignEvent implements Cancellable {

        private boolean isCancelled;

        public Pre(Player p, ItemStack is) {
            super(p, is);
        }

        @Override
        public boolean isCancelled() {
            return isCancelled;
        }

        @Override
        public void setCancelled(boolean cancel) {
            isCancelled = cancel;
        }

    }

    public static class Post extends PresentSignEvent {

        public Post(Player player, ItemStack is) {
            super(player, is);
        }
    }
}
