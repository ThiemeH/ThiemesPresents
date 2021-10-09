package nl.thieme.tp.events;

import nl.thieme.tp.Main;
import nl.thieme.tp.configs.MainConfig;
import nl.thieme.tp.configs.MessageConfig;
import nl.thieme.tp.models.PresentNBT;
import nl.thieme.tp.utils.HeadUtil;
import nl.thieme.tp.utils.MsgUtil;
import nl.thieme.tp.utils.PresentUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class ChatEvent implements Listener {

    private static HashMap<Player, ItemStack> signingList = new HashMap<>();
    private static HashMap<Player, BukkitTask> taskMap = new HashMap<>();

    public static void addForSigning(Player p, ItemStack is) {
        MsgUtil.sendMessage(p, MessageConfig.MessageKey.SIGN_NOW, false);
        signingList.put(p, is);
        BukkitTask bukkitTask = Bukkit.getScheduler().runTaskLater(Main.INSTANCE, () -> {
            if (signingList.containsKey(p)) {
                signingList.remove(p);
                MsgUtil.sendMessage(p, MessageConfig.MessageKey.SIGN_TIMEOUT, false);
            }
        }, MainConfig.ConfigKey.TIMEOUT_SECONDS.getInt() * 20L);
        taskMap.put(p, bukkitTask);
    }

    private static void removeTask(Player p) {
        if (taskMap.containsKey(p)) {
            taskMap.get(p).cancel();
            taskMap.remove(p);
        }
    }

    public static boolean isSignCooldown(Player p) {
        return signingList.containsKey(p);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (signingList.containsKey(e.getPlayer())) {
            e.setCancelled(true);
            ItemStack is = signingList.get(e.getPlayer());
            if (is == null || e.getMessage().equalsIgnoreCase(MessageConfig.MessageKey.CANCEL_KEYWORD.get())) {
                cancelSigning(e.getPlayer(), MessageConfig.MessageKey.SIGN_CANCEL);
                return;
            }
            if (e.getMessage().length() > MainConfig.ConfigKey.SIGN_CHARACTER_LIMIT.getInt()) {
                cancelSigning(e.getPlayer(), MessageConfig.MessageKey.SIGN_LIMIT);
                return;
            }

            String msg = MsgUtil.replaceColors(MessageConfig.MessageKey.SIGN_TO.get().replaceAll(MsgUtil.toKey, e.getMessage()));
            is.setItemMeta(HeadUtil.addLore(is.getItemMeta(), msg));
            is.setItemMeta(addSignedNBT(is));
            cancelSigning(e.getPlayer(), MessageConfig.MessageKey.SIGN_SUCCESS);
        }
    }

    private void cancelSigning(Player p, MessageConfig.MessageKey key) {
        MsgUtil.sendMessage(p, key, false);
        signingList.remove(p);
        removeTask(p);
    }

    private ItemMeta addSignedNBT(ItemStack is) {
        PresentNBT presentNBT = PresentUtil.getPresentNBT(is);
        presentNBT.isSigned = true;
        return PresentUtil.setPresentMeta(is.getItemMeta(), presentNBT);
    }
}
