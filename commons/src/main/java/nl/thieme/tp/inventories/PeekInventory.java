package nl.thieme.tp.inventories;

import nl.thieme.tp.configs.MessageConfig;
import nl.thieme.tp.models.PresentInventory;
import nl.thieme.tp.models.PresentNBT;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class PeekInventory {

    private final PresentNBT presentNBT;
    private final Inventory inventory;
    private final int size = 3 * 9;

    public PeekInventory(PresentNBT presentNBT) {
        this.presentNBT = presentNBT;
        this.inventory = Bukkit.createInventory(null, size, MessageConfig.MessageKey.PEEK_TITLE.get());
    }

    public Inventory getInventory() {
        for (int i = 0; i < size; i++) {
            if (i == 13) continue;
            inventory.setItem(i, PresentInventory.blockedSlotItemStack);
        }
        inventory.setItem(13, presentNBT.getPresent());
        return inventory;
    }

}
