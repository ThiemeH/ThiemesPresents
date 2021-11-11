package nl.thieme.tp.models;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import nl.thieme.tp.ThiemesPresents;
import nl.thieme.tp.utils.MsgUtil;
import nl.thieme.tp.utils.PresentUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static nl.thieme.tp.utils.HeadUtil.setHeadUrl;

public class Present {

    private final String name;
    private final NamespacedKey namespacedKey;
    private final PresentNBT presentNBT;
    private ShapedRecipe recipe;
    private ItemStack presentItemStack;

    public Present(String name, ItemStack stack) {
        this.presentItemStack = stack;
        this.name = name.toLowerCase();
        namespacedKey = NamespacedKey.minecraft(this.name);
        presentNBT = new PresentNBT();
        updatePresentNBT();
    }

    public void setRecipe(String[] shape, HashMap<Character, Material> ingredients) {
        recipe = new ShapedRecipe(namespacedKey, presentItemStack);
        recipe.shape(shape);
        for (String s : shape) {
            MsgUtil.debugInfo("Found shape: " + s);
        }
        for (Character key : ingredients.keySet()) {
            MsgUtil.debugInfo("Found ingredient: " + key + ": " + ingredients.get(key).toString());
            recipe.setIngredient(key, ingredients.get(key));
        }
        if (Bukkit.getRecipesFor(presentItemStack).size() != 0) {
            removeRecipe();
        }
        Bukkit.addRecipe(recipe);
    }

    public void removeRecipe() {
        ThiemesPresents.WRAPPER.removeRecipe(presentItemStack, namespacedKey);
    }

    public void setClosedHeadUrl(String endpoint) {
        presentNBT.closedHead = endpoint;
        updatePresentNBT();
        presentItemStack.setItemMeta(setHeadUrl(endpoint, presentItemStack.getItemMeta()));
    }

    public void setDisplayName(String name) {
        ItemMeta im = presentItemStack.getItemMeta();
        im.setDisplayName(name);
        presentItemStack.setItemMeta(im);
    }

    public void setLore(List<String> list) {
        ItemMeta im = presentItemStack.getItemMeta();
        List<String> result = new ArrayList<>();
        for (String s : list) {
            result.add(MsgUtil.replaceColors(s));
        }
        im.setLore(result);
        presentItemStack.setItemMeta(im);
    }

    public void addLore(List<String> list) {
        ItemMeta im = presentItemStack.getItemMeta();
        List<String> result = new ArrayList<>();
        for (String s : list) {
            result.add(MsgUtil.replaceColors(s));
        }
        if (im.getLore() != null) {
            im.getLore().addAll(result);
        } else {
            im.setLore(result);
        }
        presentItemStack.setItemMeta(im);
    }

    public void addLore(String line) {
        ItemMeta im = presentItemStack.getItemMeta();
        line = MsgUtil.replaceColors(line);
        if (im.getLore() != null) {
            im.getLore().add(line);
        } else {
            im.setLore(Collections.singletonList(line));
        }
        presentItemStack.setItemMeta(im);
    }

    public String getName() {
        return name;
    }

    public void updatePresentNBT() {
        presentItemStack = NBTEditor.set(presentItemStack, PresentUtil.presentNBTToString(presentNBT), PresentUtil.presentNBTKey);
    }

    public void setOpenHeadUrl(String endpoint) {
        presentNBT.openHead = endpoint; // open is optional
        updatePresentNBT();
        presentItemStack.setItemMeta(setHeadUrl(endpoint, presentItemStack.getItemMeta()));
    }

    public ItemStack getPresentItemStack() {
        return presentItemStack;
    }
}
