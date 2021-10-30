package nl.thieme.tp.models;

import nl.thieme.tp.configs.MainConfig;
import nl.thieme.tp.configs.MessageConfig;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class PresentInventory {

    public static ItemStack confirmSlotItemStack;
    public static ItemStack pendingSlotItemStack;
    public static List<Integer> blockedSlots = new ArrayList<>();
    public static Integer toBeWrappedSlot;
    public static Integer confirmSlot;
    public static ItemStack blockedSlotItemStack;
    protected Inventory inventory;
    protected HumanEntity whoOpened;

    public PresentInventory(Inventory inv, HumanEntity p) {
        inventory = inv;
        whoOpened = p;
    }

    public static void initConfigItemStacks() {
        // Blocked Item Stack
        blockedSlotItemStack = new ItemStack(MainConfig.ConfigKey.BLOCKED_SLOT_MATERIAL.getItemStack());
        ItemMeta im = blockedSlotItemStack.getItemMeta();
        im.setDisplayName(ChatColor.DARK_GRAY + "");
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        blockedSlotItemStack.setItemMeta(im);

        // Pending Item Stack
        pendingSlotItemStack = new ItemStack(MainConfig.ConfigKey.PENDING_MATERIAL.getItemStack());
        ItemMeta pim = pendingSlotItemStack.getItemMeta();
        pim.setDisplayName(MessageConfig.MessageKey.CHOOSE_ITEM.get());
        pendingSlotItemStack.setItemMeta(pim);

        // Confirm Item Stack
        confirmSlotItemStack = new ItemStack(MainConfig.ConfigKey.CONFIRM_MATERIAL.getItemStack());
        ItemMeta cim = confirmSlotItemStack.getItemMeta();
        cim.setDisplayName(MessageConfig.MessageKey.CLICK_CONFIRM.get());
        confirmSlotItemStack.setItemMeta(cim);
    }

    public void initializeInventoryItems() {
        List<Integer> blocked = blockedSlots();
        if (blocked != null) {
            // Remove reserved slots
            blocked.remove(Integer.valueOf(confirmSlot()));
            blocked.remove(Integer.valueOf(itemToBeWrappedSlot()));

            // Set blocked slots
            for (Integer i : blocked) {
                inventory.setItem(i, blockedSlotItemStack);
            }
        }
        if (confirmSlot() != -1) inventory.setItem(confirmSlot(), pendingSlotItemStack);
        if (itemToBeWrappedSlot() != -1) inventory.setItem(itemToBeWrappedSlot(), null);

        // For inventory click
        toBeWrappedSlot = itemToBeWrappedSlot();
        confirmSlot = confirmSlot();
        blockedSlots = blocked;
    }

    public List<Integer> blockedSlots() {
        return null;
    }

    public int confirmSlot() {
        return -1;
    }

    public int itemToBeWrappedSlot() {
        return -1;
    }

    public Inventory getInventory() {
        initializeInventoryItems();
        return inventory;
    }
}
