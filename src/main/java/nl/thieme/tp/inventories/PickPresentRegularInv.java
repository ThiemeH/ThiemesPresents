package nl.thieme.tp.inventories;

import nl.thieme.tp.configs.MessageConfig;
import nl.thieme.tp.models.PresentInventory;
import nl.thieme.tp.utils.InvUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;

import java.util.ArrayList;
import java.util.List;

public class PickPresentRegularInv extends PresentInventory {

    private final int size = 9 * 5;

    public PickPresentRegularInv(HumanEntity p) {
        super(Bukkit.createInventory(null, 9 * 5, MessageConfig.MessageKey.PICK_PRESENT_TITLE.get()), p);
    }

    @Override
    public void initializeInventoryItems() {
        for (int i = 0; i < 36; i++) {
            inventory.setItem(InvUtil.getSlotThiemeWay(i), whoOpened.getInventory().getContents()[i]);
        }
        super.initializeInventoryItems();
    }

    @Override
    public int confirmSlot() {
        return size - 1;
    }

    @Override
    public int itemToBeWrappedSlot() {
        return size - 5;
    }

    @Override
    public List<Integer> blockedSlots() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            list.add(size - (i + 1));
        }
        return list;
    }
}
