package nl.thieme.tp.utils;

import nl.thieme.tp.ThiemesPresents;
import nl.thieme.tp.configs.MainConfig;
import nl.thieme.tp.configs.MessageConfig;
import nl.thieme.tp.events.custom.PresentSignEvent;
import nl.thieme.tp.models.PresentNBT;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

public class SigningUtil {

    private static final HashMap<UUID, ItemStack> signingList = new HashMap<>();
    private static final HashMap<UUID, BukkitTask> taskMap = new HashMap<>();

    public static boolean canBeSigned(ItemStack is, Player p) {
        if (!MainConfig.ConfigKey.CAN_SIGN.getBoolean()) return false; // Check if signed or can be signed
        PresentNBT nbt = PresentUtil.getPresentNBT(is);
        if (!nbt.isSigned && !SigningUtil.isSignCooldown(p)) return true;
        return false;
    }

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

    public static ItemStack getItemSigning(Player p) {
        return signingList.get(p.getUniqueId());
    }

    public static void doneSigning(Player p, MessageConfig.MessageKey key) {
        if (key != null) MsgUtil.sendMessage(p, key, false);
        signingList.remove(p.getUniqueId());
        removeTask(p);
    }

    public static void doneSigning(Player p) {
        doneSigning(p, MessageConfig.MessageKey.SIGN_UNKNOWN);
    }
}
