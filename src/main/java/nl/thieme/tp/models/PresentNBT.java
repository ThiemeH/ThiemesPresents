package nl.thieme.tp.models;

import nl.thieme.tp.utils.HeadUtil;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.io.Serializable;

public class PresentNBT implements Serializable {

    // is signed
    public boolean isSigned = false;

    // present itself
    public String presentBase64 = null;

    // closed head url
    public String closed_head = null;

    // open head url
    public String open_head = null;


    public boolean hasPresent() {
        return presentBase64 != null;
    }

    public ItemStack getPresent() {
        try {
            return hasPresent() ? HeadUtil.itemStackFromBase64(presentBase64) : null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setPresent(ItemStack is) {
        presentBase64 = HeadUtil.itemStackToBase64(is);
    }
}
