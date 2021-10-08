package nl.thieme.tp.inventories;

import nl.thieme.tp.models.PresentInventory;
import nl.thieme.tp.utils.InvUtil;
import org.bukkit.entity.HumanEntity;

import java.util.ArrayList;
import java.util.List;

public class PickPresentRegularInv extends PresentInventory {

    private int size = 9 * 5;

    public PickPresentRegularInv(HumanEntity p) {
        super(9 * 5, p);
    }

    @Override
    public void initializeInventoryItems() {
        for(int i = 0; i < 36; i++) {
            inventory.setItem(InvUtil.getSlotThiemeWay(i), whoOpened.getInventory().getContents()[i]);
        }
        super.initializeInventoryItems();
    }

    @Override
    public int confirmSlot() {
        return size -1;
    }

    @Override
    public int itemToBeWrappedSlot() {
        return size - 5;
    }

    @Override
    public List<Integer> blockedSlots() {
        List<Integer> list = new ArrayList<>();
        for(int i = 0; i < 9; i++) {
            list.add(size-(i+1));
        }
        return list;
    }
}
