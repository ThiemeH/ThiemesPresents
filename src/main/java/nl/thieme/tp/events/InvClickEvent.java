package nl.thieme.tp.events;

import nl.thieme.tp.configs.MainConfig;
import nl.thieme.tp.configs.MessageConfig;
import nl.thieme.tp.utils.MsgUtil;
import nl.thieme.tp.utils.PresentUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InvClickEvent implements Listener {


    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        // Anvil
        if (!MainConfig.ConfigKey.RENAME_ANVIL.getBoolean() && isAnvilScreenWithPresent(e.getClickedInventory(), e.getCurrentItem())) {
            MsgUtil.sendMessage(e.getWhoClicked(), MessageConfig.MessageKey.NO_ANVIL_RENAME);
            e.setCancelled(true);
            return;
        }



    }

    private boolean isAnvilScreenWithPresent(Inventory inv, ItemStack is) {
        if (inv instanceof AnvilInventory) {
            return PresentUtil.isPresentItemStack(is);
        }
        return false;
    }
}
