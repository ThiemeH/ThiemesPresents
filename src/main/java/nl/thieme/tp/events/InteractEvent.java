package nl.thieme.tp.events;

import nl.thieme.tp.configs.MainConfig;
import nl.thieme.tp.models.PresentNBT;
import nl.thieme.tp.utils.InvUtil;
import nl.thieme.tp.utils.PresentUtil;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InteractEvent implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        ItemStack is = e.getPlayer().getInventory().getItemInMainHand();
        if (is == null || is.getType() == Material.AIR || !is.hasItemMeta()) return;
        if (!PresentUtil.isPresentItemStack(is)) return;

        // Handle present
        PresentNBT nbt = PresentUtil.getPresentNBT(is);

        if (isLeftClick(e.getAction()) && nbt.hasPresent()) { // Left click is only for writing messages
            if(nbt.isSigned || !MainConfig.ConfigKey.CAN_SIGN.getBoolean()) return; // Check if signed or can be signed
            e.getPlayer().sendMessage("Signing now!");

        } else if (isRightClick(e.getAction())) { // Right click for wrapping and opening
            e.setCancelled(true);
            if (!nbt.hasPresent()) {
                InvUtil.openPresentPickInventory(e.getPlayer());
            } else {
                PresentUtil.open(is, e.getPlayer());
            }
        }

    }

    private boolean isRightClick(Action a) {
        return a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK;
    }

    private boolean isLeftClick(Action a) {
        return a == Action.LEFT_CLICK_BLOCK || a == Action.LEFT_CLICK_AIR;
    }

}
