package nl.thieme.tp.wrappers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public class TPWrapper_Up implements ITPWrapper {
    @Override
    public ItemStack getSkullItemStack() {
        return new ItemStack(Material.PLAYER_HEAD);
    }

    @Override
    public void removeRecipe(ItemStack stack, NamespacedKey key) {
        Bukkit.removeRecipe(key);
    }

    @Override
    public ItemStack getOrangeConcreteItemStack() {
        return new ItemStack(Material.ORANGE_CONCRETE);
    }

    @Override
    public ItemStack getLimeConcreteItemStack() {
        return new ItemStack(Material.LIME_CONCRETE);
    }

    @Override
    public ItemStack getGrayStainedGlass() {
        return new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
    }
}
