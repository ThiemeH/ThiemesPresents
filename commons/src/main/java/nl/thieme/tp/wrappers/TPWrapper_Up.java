package nl.thieme.tp.wrappers;

import nl.thieme.tp.models.ITPWrapper;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public class TPWrapper_Up implements ITPWrapper {

    @Override
    public void removeRecipe(ItemStack stack, NamespacedKey key) {
        Bukkit.removeRecipe(key);
    }

}
