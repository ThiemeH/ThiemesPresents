package nl.thieme.tp.utils;

import nl.thieme.tp.configs.MessageConfig;
import nl.thieme.tp.inventories.PickPresentRegularInv;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.HashMap;
import java.util.UUID;

public class InvUtil {

    private static final HashMap<UUID, ItemStack[]> backupInventory = new HashMap<>();

    public static int getSlotThiemeWay(int i) {
        if (i < 9) {
            return 36 - 9 + i;
        } else {
            return i - 9;
        }

    }

    public static void openPresentPickInventory(HumanEntity p) {
        // Later possible add multiple inventories
        p.openInventory(new PickPresentRegularInv(p).getInventory());
    }

    private static void resetInventory(Inventory inventory, ItemStack[] itemStacks) {
        for (int i = 0; i < itemStacks.length; i++) {
            // Only update empty slots, as full slots can't be moved
            if (inventory.getItem(i) == null || inventory.getItem(i).getType() == Material.AIR) {
                inventory.setItem(i, itemStacks[i]);
            }
        }
    }


    public static void refreshInventory(Inventory inv, HumanEntity he) {
        if (hasBackup(he.getUniqueId())) {
            resetInventory(inv, backupInventory.get(he.getUniqueId()));
        } else {
            openPresentPickInventory(he);
        }
    }

    public static boolean isPresentInventory(InventoryView view) {
        return view.getTitle().equalsIgnoreCase(MessageConfig.MessageKey.PICK_PRESENT_TITLE.get());
    }

    public static void removeBackup(UUID uuid) {
        if (hasBackup(uuid)) backupInventory.remove(uuid);
    }

    public static boolean hasBackup(UUID uuid) {
        return backupInventory.containsKey(uuid);
    }

    public static void createBackup(UUID uuid, ItemStack[] inv) {
        backupInventory.put(uuid, inv);
    }


    public static boolean isStorageItem(ItemStack is) {
        if (is.getItemMeta() instanceof BlockStateMeta) {
            BlockStateMeta bMeta = (BlockStateMeta) is.getItemMeta();
            if (bMeta.getBlockState() instanceof Container) return true;
        }
        return false;
    }

    public static boolean isPresentPeekInventory(InventoryView view) {
        return view.getTitle().equalsIgnoreCase(MessageConfig.MessageKey.PEEK_TITLE.get());
    }

    public static void removeAllBackups() {
        for (UUID uuid : backupInventory.keySet()) {
            if (Bukkit.getPlayer(uuid) == null) continue;
            if (Bukkit.getPlayer(uuid).getOpenInventory() == null) continue;
            InventoryView inv = Bukkit.getPlayer(uuid).getOpenInventory();
            if (isPresentInventory(inv) || isPresentPeekInventory(inv)) {
                Bukkit.getPlayer(uuid).closeInventory();
            }
        }
    }
}
