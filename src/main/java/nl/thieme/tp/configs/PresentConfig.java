package nl.thieme.tp.configs;

import nl.thieme.tp.Main;
import nl.thieme.tp.models.FileConfig;
import nl.thieme.tp.models.Present;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PresentConfig extends FileConfig {

    private ArrayList<Present> presents = new ArrayList<>();

    private final String itemNameKey = "ITEM_NAME";
    private final String headUrlKey = "HEAD_URL";
    private final String recipeKey = "RECIPE";
    private final String shapeKey = "SHAPE";
    private final String ingredientsKey = "INGREDIENTS";

    public PresentConfig(String name) {
        super(name);
        loadPresents();
    }

    private void loadPresents() {
        for(String root : config.getKeys(false)) {
            if(Main.DEBUG) Main.LOGGER.info("Found present: " + root);
            ConfigurationSection base = config.getConfigurationSection(root);
            Present present = new Present(root);

            // Load properties
            if(base.contains(recipeKey)) loadRecipe(present, base.getConfigurationSection(recipeKey));
            if(base.contains(headUrlKey)) present.setHeadUrl(base.getString(headUrlKey));
            if(base.contains(itemNameKey)) present.setDisplayName(base.getString(itemNameKey).replaceAll("&", "§"));

        }
    }

    private void loadRecipe(Present present, ConfigurationSection section) {
        if(!section.contains(shapeKey) && !section.contains(ingredientsKey)) {
            Main.LOGGER.warning("Recipe is missing either ingredients or shape");
            return;
        }

        List<String> shape = section.getStringList(shapeKey);
        ConfigurationSection ingredientSection = section.getConfigurationSection(ingredientsKey);
        HashMap<Character, String> ingredientMap = new HashMap<>();
        for(String ingredient : ingredientSection.getKeys(false)) {
            ingredientMap.put(ingredient.charAt(0), ingredientSection.getString(ingredient));
        }

        present.setRecipe(shape, ingredientMap);
    }

}
