package nl.thieme.tp.models;

import nl.thieme.tp.Main;
import nl.thieme.tp.utils.MsgUtil;
import nl.thieme.tp.utils.NBTEditor;
import nl.thieme.tp.utils.PresentUtil;
import nl.thieme.tp.utils.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static nl.thieme.tp.utils.HeadUtil.setHeadUrl;

public class Present {

    private final String name;
    private final NamespacedKey namespacedKey;
    private final PresentNBT presentNBT;
    private ShapedRecipe recipe;
    private ItemStack presentStack;

    public Present(String name, ItemStack stack) {
        this.presentStack = stack;
        this.name = name.toLowerCase();
        namespacedKey = NamespacedKey.minecraft(this.name);
        presentNBT = new PresentNBT();
        updatePresentNBT();
    }

    public void setRecipe(String[] shape, HashMap<Character, String> ingredients) {
        recipe = new ShapedRecipe(namespacedKey, presentStack);
        recipe.shape(shape);
        for (String s : shape) {
            MsgUtil.debugInfo("Found shape: " + s);
        }
        for (Character key : ingredients.keySet()) {
            MsgUtil.debugInfo("Found ingredient: " + key + ": " + ingredients.get(key));
            String ing = ingredients.get(key);

            XMaterial m = XMaterial.matchXMaterial(ing).get();
            if (m == null) {
                Main.LOGGER.warning(ing + " IS NOT A VALID MATERIAL! RECIPE DISABLED");
                return;
            }
            recipe.setIngredient(key, m.parseItem().getData());
        }
        if (Bukkit.getRecipesFor(presentStack).size() != 0) {
            removeRecipe();
        }
        Bukkit.addRecipe(recipe);
    }

    public void removeRecipe() {
        Main.WRAPPER.removeRecipe(presentStack, namespacedKey);
    }

    public void setClosedHeadUrl(String endpoint) {
        presentNBT.closed_head = endpoint;
        updatePresentNBT();
        presentStack.setItemMeta(setHeadUrl(endpoint, presentStack.getItemMeta()));
    }

    public void setDisplayName(String name) {
        ItemMeta im = presentStack.getItemMeta();
        im.setDisplayName(name);
        presentStack.setItemMeta(im);
    }

    public void setLore(List<String> list) {
        ItemMeta im = presentStack.getItemMeta();
        List<String> result = new ArrayList<>();
        for (String s : list) {
            result.add(MsgUtil.replaceColors(s));
        }
        im.setLore(result);
        presentStack.setItemMeta(im);
    }

    public String getName() {
        return name;
    }

    private void updatePresentNBT() {
        presentStack = NBTEditor.set(presentStack, PresentUtil.presentNBTToString(presentNBT), PresentUtil.presentNBTKey);
    }

    public void setOpenHeadUrl(String endpoint) {
        presentNBT.open_head = endpoint; // open is optional
        updatePresentNBT();
        presentStack.setItemMeta(setHeadUrl(endpoint, presentStack.getItemMeta()));
    }

    public ItemStack getPresentStack() {
        return presentStack;
    }


}
