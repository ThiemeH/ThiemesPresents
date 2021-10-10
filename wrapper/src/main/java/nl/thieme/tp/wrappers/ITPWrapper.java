package nl.thieme.tp.wrappers;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public interface ITPWrapper {


    ItemStack getSkullItemStack();

    void removeRecipe(ItemStack stack, NamespacedKey key);

    ItemStack getOrangeConcreteItemStack();
    ItemStack getLimeConcreteItemStack();
    ItemStack getGrayStainedGlass();
}
