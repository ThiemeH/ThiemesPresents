package nl.thieme.tp.events.custom;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PresentOpenEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final ItemStack presentItemStack;
    private final Player player;

    public PresentOpenEvent(ItemStack presentItemStack, Player player) {
        this.presentItemStack = presentItemStack;
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getPresentItemStack() {
        return presentItemStack;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static class Pre extends PresentOpenEvent implements Cancellable {

        private boolean isCancelled;

        public Pre(ItemStack presentItemStack, Player player) {
            super(presentItemStack, player);
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

    public static class Post extends PresentOpenEvent {

        private final ItemStack insidePresentStack;

        public Post(ItemStack presentItemStack, ItemStack insidePresentStack, Player player) {
            super(presentItemStack, player);
            this.insidePresentStack = insidePresentStack;
        }

        public ItemStack getItem() {
            return insidePresentStack;
        }
    }
}
