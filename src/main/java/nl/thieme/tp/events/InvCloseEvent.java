package nl.thieme.tp.events;

import nl.thieme.tp.utils.InvUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InvCloseEvent implements Listener {

    @EventHandler
    public void onInvClose(InventoryCloseEvent e) {
        if(InvUtil.isPresentInventory(e.getView())) {
            InvUtil.removeBackup(e.getPlayer().getUniqueId());
        }
    }
}
