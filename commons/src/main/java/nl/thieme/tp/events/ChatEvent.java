package nl.thieme.tp.events;

import nl.thieme.tp.ThiemesPresents;
import nl.thieme.tp.configs.MainConfig;
import nl.thieme.tp.configs.MessageConfig;
import nl.thieme.tp.events.custom.PresentSignEvent;
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
        PresentSignEvent.Pre pse = new PresentSignEvent.Pre(p, is);
        Bukkit.getPluginManager().callEvent(pse);
        if (pse.isCancelled()) return;

        UUID uuid = p.getUniqueId();
        MsgUtil.sendMessage(p, MessageConfig.MessageKey.SIGN_NOW, false);
        signingList.put(uuid, is);
        BukkitTask bukkitTask = Bukkit.getScheduler().runTaskLater(ThiemesPresents.INSTANCE, () -> {
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
                doneSigning(e.getPlayer(), MessageConfig.MessageKey.SIGN_CANCEL);
                return;
            }
            if (e.getMessage().length() > MainConfig.ConfigKey.SIGN_CHARACTER_LIMIT.getInt()) {
                doneSigning(e.getPlayer(), MessageConfig.MessageKey.SIGN_LIMIT);
                return;
            }
            Bukkit.getScheduler().runTask(ThiemesPresents.INSTANCE, task -> { // Back to main thread
                PresentSignEvent pse = new PresentSignEvent.Post(e.getPlayer(), is, e.getMessage());
                Bukkit.getPluginManager().callEvent(pse);
                if (pse.isCancelled()) {
                    doneSigning(e.getPlayer(), null);
                    task.cancel();
                    return;
                }
                String msg = MsgUtil.replaceColors(MessageConfig.MessageKey.SIGN_TO.get().replaceAll(MsgUtil.toKey, e.getMessage()));
                is.setItemMeta(HeadUtil.addLore(is.getItemMeta(), msg));
                addSignedNBT(is);
                doneSigning(e.getPlayer(), MessageConfig.MessageKey.SIGN_SUCCESS);
            });



        }
    }

    private void doneSigning(Player p, MessageConfig.MessageKey key) {
        if(key != null) MsgUtil.sendMessage(p, key, false);
        signingList.remove(p.getUniqueId());
        removeTask(p);
    }

    private ItemStack addSignedNBT(ItemStack is) {
        PresentNBT presentNBT = PresentUtil.getPresentNBT(is);
        presentNBT.isSigned = true;
        return PresentUtil.setPresentMeta(is, presentNBT);
    }
}
