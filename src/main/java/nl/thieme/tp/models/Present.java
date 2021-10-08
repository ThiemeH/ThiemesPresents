package nl.thieme.tp.models;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import nl.thieme.tp.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Present extends ItemStack {

    private final String minecraft_head_url_base = "https://textures.minecraft.net/texture/";
    private ShapedRecipe recipe;
    private NamespacedKey namespacedKey;
    private final String name;

    public Present(String name) {
        super(Material.PLAYER_HEAD);
        this.name = name.toLowerCase();
        namespacedKey = NamespacedKey.minecraft(this.name);
    }

    public void setRecipe(String[] shape, HashMap<Character, String> ingredients) {
        recipe = new ShapedRecipe(namespacedKey, this);
        recipe.shape(shape);
        for(Character key : ingredients.keySet()) {
            String ing = ingredients.get(key);
            Material m = Material.getMaterial(ing);
            if(m == null) {
                Main.LOGGER.warning(ing + " IS NOT A VALID MATERIAL! RECIPE DISABLED");
                return;
            }
            recipe.setIngredient(key, m);
        }

        Bukkit.addRecipe(recipe);
    }

    public void removeRecipe() {
        Bukkit.removeRecipe(namespacedKey);
    }

    public void setHeadUrl(String endpoint) {
        String url = minecraft_head_url_base + endpoint;
        if(!(getItemMeta() instanceof SkullMeta)) return;
        SkullMeta meta = (SkullMeta) getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", url));
        Field profileField;
        try {
            profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ignored) {

        }
        setItemMeta(meta);
    }

    public void setDisplayName(String name) {
        ItemMeta im = getItemMeta();
        im.setDisplayName(name);
        setItemMeta(im);
    }

    public void setLore(List<String> list) {
        ItemMeta im = getItemMeta();
        im.setLore(list);
        setItemMeta(im);
    }

    public String getName() {
        return name;
    }
}
