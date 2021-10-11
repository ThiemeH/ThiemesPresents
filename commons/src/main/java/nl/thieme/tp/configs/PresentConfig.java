package nl.thieme.tp.configs;

import com.cryptomorin.xseries.XMaterial;
import nl.thieme.tp.Main;
import nl.thieme.tp.events.custom.PresentInitEvent;
import nl.thieme.tp.models.FileConfig;
import nl.thieme.tp.models.Present;
import nl.thieme.tp.utils.MsgUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;

public class PresentConfig extends FileConfig {

    private final String loreKey = "LORE";
    private final String itemNameKey = "ITEM_NAME";
    private final String headUrlKey = "HEAD_URL";
    private final String openHeadUrlKey = "HEAD_OPEN_URL";
    private final String recipeKey = "RECIPE";
    private final String shapeKey = "SHAPE";
    private final String ingredientsKey = "INGREDIENTS";
    private final ArrayList<Present> presents = new ArrayList<>();

    public PresentConfig(String name) {
        super(name);
        loadPresents();
    }

    private void loadPresents() {
        for (String root : config.getKeys(false)) {
            if (Main.DEBUG) Main.LOGGER.info("Found present: " + root);
            ConfigurationSection base = config.getConfigurationSection(root);
            Present present = new Present(root, XMaterial.PLAYER_HEAD.parseItem());

            // Load properties
            if (base.contains(headUrlKey)) present.setClosedHeadUrl(base.getString(headUrlKey));
            if (base.contains(openHeadUrlKey)) present.setOpenHeadUrl(base.getString(openHeadUrlKey));
            if (base.contains(itemNameKey)) present.setDisplayName(MsgUtil.replaceColors(base.getString(itemNameKey)));
            if (base.contains(loreKey)) present.setLore(base.getStringList(loreKey));
            if (base.contains(recipeKey)) loadRecipe(present, base.getConfigurationSection(recipeKey)); // recipe last
            presents.add(present);
        }
        Bukkit.getPluginManager().callEvent(new PresentInitEvent(presents));
    }

    private void loadRecipe(Present present, ConfigurationSection section) {
        if (!section.contains(shapeKey) && !section.contains(ingredientsKey)) {
            Main.LOGGER.warning("Recipe is missing either ingredients or shape");
            return;
        }
        String[] shape = section.getStringList(shapeKey).toArray(new String[0]);
        ConfigurationSection ingredientSection = section.getConfigurationSection(ingredientsKey);
        HashMap<Character, String> ingredientMap = new HashMap<>();
        for (String ingredient : ingredientSection.getKeys(false)) {
            ingredientMap.put(ingredient.charAt(0), ingredientSection.getString(ingredient));
        }

        present.setRecipe(shape, ingredientMap);
    }

    public ArrayList<Present> getPresents() {
        return presents;
    }

}
