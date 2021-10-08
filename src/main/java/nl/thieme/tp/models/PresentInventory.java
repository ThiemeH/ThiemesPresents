package nl.thieme.tp.models;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class PresentInventory {

    private Inventory inventory;

    public PresentInventory(InventoryType type) {
        inventory = Bukkit.createInventory(null, type);
    }

    public int confirmSlot() {
        return -1;
    }


    public Inventory getInventory() {
        return inventory;
    }

}
