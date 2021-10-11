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
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

public class ChatEvent implements Listener {

    private static final HashMap<UUID, ItemStack> signingList = new HashMap<>();
    private static final HashMap<UUID, BukkitTask> taskMap = new HashMap<>();

    public static void addForSigning(Player p, ItemStack is) {
        UUID uuid = p.getUniqueId();
        MsgUtil.sendMessage(p, MessageConfig.MessageKey.SIGN_NOW, false);
        signingList.put(uuid, is);
        BukkitTask bukkitTask = Bukkit.getScheduler().runTaskLater(Main.INSTANCE, () -> {
            if (signingList.containsKey(uuid)) {
                signingList.remove(uuid);
                MsgUtil.sendMessage(p, MessageConfig.MessageKey.SIGN_TIMEOUT, false);
            }
        }, MainConfig.ConfigKey.TIMEOUT_SECONDS.getInt() * 20L);
        taskMap.put(uuid, bukkitTask);
    }

    private static void removeTask(Player p) {
        UUID uuid = p.getUniqueId();
        if (taskMap.containsKey(uuid)) {
            taskMap.get(uuid).cancel();
            taskMap.remove(uuid);
        }
    }

    public static boolean isSignCooldown(Player p) {
        return signingList.containsKey(p.getUniqueId());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (signingList.containsKey(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            ItemStack is = signingList.get(e.getPlayer().getUniqueId());
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
            addSignedNBT(is);
            cancelSigning(e.getPlayer(), MessageConfig.MessageKey.SIGN_SUCCESS);
        }
    }

    private void cancelSigning(Player p, MessageConfig.MessageKey key) {
        MsgUtil.sendMessage(p, key, false);
        signingList.remove(p.getUniqueId());
        removeTask(p);
    }

    private ItemStack addSignedNBT(ItemStack is) {
        PresentNBT presentNBT = PresentUtil.getPresentNBT(is);
        presentNBT.isSigned = true;
        return PresentUtil.setPresentMeta(is, presentNBT);
    }
}
