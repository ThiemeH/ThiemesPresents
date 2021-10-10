package nl.thieme.tp.wrappers;

import nl.thieme.tp.models.ITPWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public class TPWrapper_12 implements ITPWrapper {
    @Override
    public ItemStack getSkullItemStack() {
        return new ItemStack(Material.SKULL_ITEM, 1 , (short) 3);
    }

    @Override
    public void removeRecipe(ItemStack stack, NamespacedKey key) {
        Bukkit.clearRecipes();
    }

    @Override
    public ItemStack getOrangeConcreteItemStack() {
        return new ItemStack(Material.CONCRETE, 1, (byte)1);
    }

    @Override
    public ItemStack getLimeConcreteItemStack() {
        return new ItemStack(Material.CONCRETE, 1, (byte)5);
    }

    @Override
    public ItemStack getGrayStainedGlass() {
        return new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)7);
    }
}
