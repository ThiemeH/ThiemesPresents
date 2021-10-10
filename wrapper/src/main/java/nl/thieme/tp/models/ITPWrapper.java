package nl.thieme.tp.models;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public interface ITPWrapper {

    void removeRecipe(ItemStack stack, NamespacedKey key);

}
