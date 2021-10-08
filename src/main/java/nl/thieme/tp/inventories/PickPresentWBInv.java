package nl.thieme.tp.inventories;

import nl.thieme.tp.models.PresentInventory;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

public class PickPresentWBInv extends PresentInventory {

    public PickPresentWBInv(HumanEntity p) {
        super(InventoryType.WORKBENCH, p);
    }
}
