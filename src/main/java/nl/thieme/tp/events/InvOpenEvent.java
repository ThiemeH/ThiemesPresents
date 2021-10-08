package nl.thieme.tp.events;

import nl.thieme.tp.utils.InvUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class InvOpenEvent implements Listener {

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        if(InvUtil.isPresentInventory(e.getView())) {
            InvUtil.createBackup(e.getPlayer().getUniqueId(), e.getInventory().getContents());;
        }
    }
}
