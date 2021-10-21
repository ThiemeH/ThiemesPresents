package nl.thieme.tp.configs;

import com.cryptomorin.xseries.XMaterial;
import nl.thieme.tp.ThiemesPresents;
import nl.thieme.tp.models.FileConfig;
import nl.thieme.tp.models.Present;
import nl.thieme.tp.utils.MsgUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PresentConfig extends FileConfig {

    private final String loreKey = "LORE";
    private final String itemNameKey = "ITEM_NAME";
    private final String headUrlKey = "HEAD_URL";
    private final String openHeadUrlKey = "HEAD_OPEN_URL";
    private final String recipeKey = "RECIPE";
    private final String shapeKey = "SHAPE";
    private final String ingredientsKey = "INGREDIENTS";

    public PresentConfig(String name) {
        super(name);
        loadPresents();
    }

    private void loadPresents() {
        List<Present> presentList = new ArrayList<>();
        for (String root : config.getKeys(false)) {
            MsgUtil.debugInfo("Found present: " + root);
            ConfigurationSection base = config.getConfigurationSection(root);
            Present present = new Present(root, XMaterial.PLAYER_HEAD.parseItem());
            if(MessageConfig.MessageKey.LORE_OPEN.get() != null) present.addLore(MessageConfig.MessageKey.LORE_OPEN.get());

            // Load properties
            if (base.contains(headUrlKey)) present.setClosedHeadUrl(base.getString(headUrlKey));
            if (base.contains(openHeadUrlKey)) present.setOpenHeadUrl(base.getString(openHeadUrlKey));
            if (base.contains(itemNameKey)) present.setDisplayName(MsgUtil.replaceColors(base.getString(itemNameKey)));
            if (base.contains(loreKey)) present.addLore(base.getStringList(loreKey));
            if (base.contains(recipeKey)) loadRecipe(present, base.getConfigurationSection(recipeKey)); // recipe last
            presentList.add(present);
        }
        ThiemesPresents.getPresentManager().addPresents(presentList);
    }

    private void loadRecipe(Present present, ConfigurationSection section) {
        if (!section.contains(shapeKey) && !section.contains(ingredientsKey)) {
            ThiemesPresents.LOGGER.warning("Recipe is missing either ingredients or shape");
            return;
        }
        String[] shape = section.getStringList(shapeKey).toArray(new String[0]);
        ConfigurationSection ingredientSection = section.getConfigurationSection(ingredientsKey);
        HashMap<Character, Material> ingredientMap = new HashMap<>();
        for (String ingredient : ingredientSection.getKeys(false)) {
            String ing = ingredientSection.getString(ingredient);
            XMaterial m = XMaterial.matchXMaterial(ing).get();
            if (m == null) {
                ThiemesPresents.LOGGER.warning(ing + " IS NOT A VALID MATERIAL! RECIPE DISABLED");
                return;
            }
            ingredientMap.put(ingredient.charAt(0), m.parseMaterial());
        }

        present.setRecipe(shape, ingredientMap);
    }
}
