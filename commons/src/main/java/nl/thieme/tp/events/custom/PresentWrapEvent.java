package nl.thieme.tp.events.custom;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PresentWrapEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final ItemStack presentItemStack;
    private final ItemStack toBeWrappedStack;
    private final Player player;

    public PresentWrapEvent(ItemStack presentItemStack, ItemStack toBeWrappedStack, Player player) {
        this.presentItemStack = presentItemStack;
        this.toBeWrappedStack = toBeWrappedStack;
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static class Pre extends PresentWrapEvent implements Cancellable {

        private boolean isCancelled;

        public Pre(ItemStack presentItemStack, ItemStack toBeWrappedStack, Player player) {
            super(presentItemStack, toBeWrappedStack, player);
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

    public static class Post extends PresentWrapEvent {


        public Post(ItemStack presentItemStack, ItemStack toBeWrappedStack, Player player) {
            super(presentItemStack, toBeWrappedStack, player);
        }

    }
}
