package nl.thieme.tp.models;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
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

    public Present(String name) {
        super(Material.PLAYER_HEAD);
        namespacedKey = NamespacedKey.minecraft(name);
    }

    public void setRecipe(List<String> shape, HashMap<Character, String> ingredients) {
        recipe = new ShapedRecipe(namespacedKey, this);
        recipe.shape(String.valueOf(shape));
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
}
