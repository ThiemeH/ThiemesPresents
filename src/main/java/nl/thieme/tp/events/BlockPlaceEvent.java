package nl.thieme.tp.events;

import nl.thieme.tp.utils.PresentUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BlockPlaceEvent implements Listener {

    @EventHandler
    public void onBlockPlace(org.bukkit.event.block.BlockPlaceEvent e) {
        if (PresentUtil.isPresentItemStack(e.getItemInHand())) e.setCancelled(true);
    }
}
