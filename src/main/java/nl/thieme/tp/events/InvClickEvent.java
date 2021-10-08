package nl.thieme.tp.events;

import nl.thieme.tp.configs.MainConfig;
import nl.thieme.tp.configs.MessageConfig;
import nl.thieme.tp.models.PresentInventory;
import nl.thieme.tp.utils.InvUtil;
import nl.thieme.tp.utils.MsgUtil;
import nl.thieme.tp.utils.PresentUtil;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class InvClickEvent implements Listener {


    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        // Anvil
        if (!MainConfig.ConfigKey.RENAME_ANVIL.getBoolean() && isAnvilScreenWithPresent(e.getClickedInventory(), e.getCurrentItem())) {
            MsgUtil.sendMessage(e.getWhoClicked(), MessageConfig.MessageKey.NO_ANVIL_RENAME);
            e.setCancelled(true);
            return;
        }
        if (!InvUtil.isPresentInventory(e.getView())) return;
        e.setCancelled(true);

        // Cancel own inventory clicks
        if (e.getClickedInventory() instanceof PlayerInventory) return;

        // Cancel blocked slots
        if (PresentInventory.blockedSlots.contains(e.getSlot())) return;


        ItemStack curItem = e.getCurrentItem();
        Inventory inv = e.getInventory();

        // Cancel presents
        if (curItem != null && PresentUtil.isPresentItemStack(curItem)) return;

        // Return air click if no item is selected
        if (curItem == null && inv.getItem(PresentInventory.toBeWrappedSlot) == null) {
            return;
        } else {
            if (e.getSlot() == PresentInventory.confirmSlot) {
                if (e.getWhoClicked() instanceof Player) {
                    Player p = (Player) e.getWhoClicked();
                    PresentUtil.wrap(p.getInventory().getItemInMainHand(), inv.getItem(PresentInventory.toBeWrappedSlot), p);
                }
                return;
            }
        }

        updateSelected(inv, curItem, e.getWhoClicked(), e.getSlot());
        e.setCurrentItem(null);
    }

    private void updateSelected(Inventory inv, ItemStack is, HumanEntity he, int slot) {
        InvUtil.refreshInventory(inv, he);
        if (inv.getItem(PresentInventory.toBeWrappedSlot) == null || (is != null && slot != PresentInventory.toBeWrappedSlot)) {
            select(inv, is, slot);
        } else {
            unselect(inv);
        }
    }

    private void unselect(Inventory inv) {
        inv.setItem(PresentInventory.confirmSlot, PresentInventory.pendingSlotItemStack);
        inv.setItem(PresentInventory.toBeWrappedSlot, null);

    }

    private void select(Inventory inv, ItemStack is, int slot) {
        inv.setItem(PresentInventory.confirmSlot, PresentInventory.confirmSlotItemStack);
        inv.setItem(PresentInventory.toBeWrappedSlot, is);
        inv.setItem(slot, null);
    }


    private boolean isAnvilScreenWithPresent(Inventory inv, ItemStack is) {
        if (inv instanceof AnvilInventory) {
            return PresentUtil.isPresentItemStack(is);
        }
        return false;
    }
}
