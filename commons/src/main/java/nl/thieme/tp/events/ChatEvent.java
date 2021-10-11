package nl.thieme.tp.events;

import nl.thieme.tp.ThiemesPresents;
import nl.thieme.tp.configs.MainConfig;
import nl.thieme.tp.configs.MessageConfig;
import nl.thieme.tp.events.custom.PresentSignEvent;
import nl.thieme.tp.utils.HeadUtil;
import nl.thieme.tp.utils.MsgUtil;
import nl.thieme.tp.utils.PresentUtil;
import nl.thieme.tp.utils.SigningUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ChatEvent implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (SigningUtil.isSignCooldown(e.getPlayer())) {
            e.setCancelled(true);
            ItemStack is = SigningUtil.getItemSigning(e.getPlayer());

            if (is == null || e.getMessage().equalsIgnoreCase(MessageConfig.MessageKey.CANCEL_KEYWORD.get())) {
                SigningUtil.doneSigning(e.getPlayer(), MessageConfig.MessageKey.SIGN_CANCEL);
                return;
            }

            if (e.getMessage().length() > MainConfig.ConfigKey.SIGN_CHARACTER_LIMIT.getInt()) {
                SigningUtil.doneSigning(e.getPlayer(), MessageConfig.MessageKey.SIGN_LIMIT);
                return;
            }

            Bukkit.getScheduler().runTask(ThiemesPresents.INSTANCE, task -> { // Back to main thread
                PresentSignEvent pse = new PresentSignEvent.Post(e.getPlayer(), is, e.getMessage());
                Bukkit.getPluginManager().callEvent(pse);

                Inventory inv = e.getPlayer().getInventory();
                // if item is still in inventory or event is cancelled
                if (pse.isCancelled() || inv.first(is) == -1 || inv.getItem(inv.first(is)) == null) {
                    SigningUtil.doneSigning(e.getPlayer());
                    task.cancel();
                    return;
                }
                // Try to add signed meta data
                ItemStack copy = inv.getItem(inv.first(is));
                if (!PresentUtil.addSignedNBT(copy)) {
                    SigningUtil.doneSigning(e.getPlayer());
                    return;
                }
                String msg = MsgUtil.replaceColors(MessageConfig.MessageKey.SIGN_TO.get().replaceAll(MsgUtil.toKey, e.getMessage()));
                copy.setItemMeta(HeadUtil.addLore(copy.getItemMeta(), msg));
                SigningUtil.doneSigning(e.getPlayer(), MessageConfig.MessageKey.SIGN_SUCCESS);
            });
        }
    }
}
