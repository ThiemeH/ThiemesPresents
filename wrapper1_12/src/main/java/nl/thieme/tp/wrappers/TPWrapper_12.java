package nl.thieme.tp.wrappers;

import nl.thieme.tp.models.ITPWrapper;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TPWrapper_12 implements ITPWrapper {

    @Override
    public void removeRecipe(ItemStack stack, NamespacedKey key) {
        List<Recipe> backup = new ArrayList<>();
        Iterator<Recipe> a = Bukkit.getServer().recipeIterator();
        // 1.12.2 does not contain a getServer().removeRecipe() method
        new Thread(() -> {
            while (a.hasNext()) {
                Recipe recipe = a.next();
                ItemStack result = recipe.getResult();
                if (!result.isSimilar(stack)) {
                    backup.add(recipe);
                }
            }
            Bukkit.getServer().clearRecipes();
            for (Recipe r : backup)
                Bukkit.getServer().addRecipe(r);
        }).start();
    }
}
