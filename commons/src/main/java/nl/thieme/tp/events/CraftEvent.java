package nl.thieme.tp.events;

import nl.thieme.tp.configs.MainConfig;
import nl.thieme.tp.configs.MessageConfig;
import nl.thieme.tp.utils.MsgUtil;
import nl.thieme.tp.utils.PresentUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

import java.util.List;

public class CraftEvent implements Listener {

    @EventHandler
    public void onCraft(CraftItemEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        if (!PresentUtil.isPresentItemStack(e.getCurrentItem())) return;
        Player p = (Player) e.getWhoClicked();
        List<String> disabledWorlds = MainConfig.ConfigKey.DISABLED_WORLDS.getStringList();
        if (disabledWorlds.contains(p.getWorld().getName())) {
            e.setCancelled(true);
            MsgUtil.sendMessage(p, MessageConfig.MessageKey.DISABLED_WORLD);
        }
    }
}
