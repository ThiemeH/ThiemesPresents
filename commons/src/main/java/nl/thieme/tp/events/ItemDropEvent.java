package nl.thieme.tp.events;

import nl.thieme.tp.configs.MessageConfig;
import nl.thieme.tp.utils.PresentUtil;
import nl.thieme.tp.utils.SigningUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ItemDropEvent implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (SigningUtil.isSignCooldown(e.getPlayer())) {
            if (!PresentUtil.isPresentItemStack(e.getItemDrop().getItemStack())) return;
            SigningUtil.doneSigning(e.getPlayer(), MessageConfig.MessageKey.SIGN_CANCEL);
        }
    }
}
