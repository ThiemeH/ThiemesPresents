package nl.thieme.tp.events;

import nl.thieme.tp.configs.MainConfig;
import nl.thieme.tp.configs.MessageConfig;
import nl.thieme.tp.models.PresentNBT;
import nl.thieme.tp.models.TPermission;
import nl.thieme.tp.utils.InvUtil;
import nl.thieme.tp.utils.MsgUtil;
import nl.thieme.tp.utils.PresentUtil;
import nl.thieme.tp.utils.SigningUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InteractEvent implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        ItemStack is = e.getPlayer().getInventory().getItemInMainHand();
        if (is.getType() == Material.AIR || !is.hasItemMeta()) return;
        if (!PresentUtil.isPresentItemStack(is)) return;

        // Handle present
        PresentNBT nbt = PresentUtil.getPresentNBT(is);
        if (nbt == null) return;

        if (isLeftClick(e.getAction())) { // Left click is only for writing messages
            handleLeftClick(e.getPlayer(), is, nbt);
        } else if (isRightClick(e.getAction())) { // Right click for wrapping and opening
            MsgUtil.debugInfo("NBT has present: " + nbt.hasPresent());
            handleRightClick(e.getPlayer(), is, nbt);
        }
    }

    private void handleRightClick(Player p, ItemStack is, PresentNBT nbt) {
        // Opening and wrapping can only be done with one present
        if (is.getAmount() > 1) {
            MsgUtil.sendMessage(p, MessageConfig.MessageKey.ONE_PRESENT);
            return;
        }

        if (!nbt.hasPresent()) {
            if (!TPermission.hasPermission(p, TPermission.NP_WRAP)) return;
            InvUtil.openPresentPickInventory(p);
        } else {
            if (!TPermission.hasPermission(p, TPermission.NP_OPEN)) return;
            PresentUtil.open(is, p);
        }
    }

    private void handleLeftClick(Player p, ItemStack is, PresentNBT nbt) {
        if (!MainConfig.ConfigKey.CAN_SIGN.getBoolean()) return;
        if (!TPermission.hasPermission(p, TPermission.NP_SIGN)) return;
        if (!nbt.hasPresent()) return;
        if (SigningUtil.canBeSigned(nbt, p)) SigningUtil.addForSigning(p, is);
    }

    private boolean isRightClick(Action a) {
        return a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK;
    }

    private boolean isLeftClick(Action a) {
        return a == Action.LEFT_CLICK_BLOCK || a == Action.LEFT_CLICK_AIR;
    }
}
