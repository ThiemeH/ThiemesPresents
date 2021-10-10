package nl.thieme.tp.configs;

import nl.thieme.tp.managers.ConfigManager;
import nl.thieme.tp.models.FileConfig;
import nl.thieme.tp.utils.XMaterial;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class MainConfig extends FileConfig {

    public MainConfig(String name) {
        super(name);
    }


    public enum ConfigKey {
        RENAME_ANVIL("can-rename-in-anvil"),
        STORAGE_WRAPPING_LIMIT("storage-item-wrapping-limit"),
        PENDING_MATERIAL("pending-material", XMaterial.ORANGE_CONCRETE.parseItem()),
        CONFIRM_MATERIAL("confirm-material", XMaterial.LIME_CONCRETE.parseItem()),
        BLOCKED_SLOT_MATERIAL("blocked-slot-material", XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()),
        CAN_SIGN("can-sign"),
        TIMEOUT_SECONDS("signing-timeout-seconds"),
        SIGN_CHARACTER_LIMIT("sign-character-limit"),
        ALLOW_STORAGE_WRAPPING("allow-wrapping-storage-items");

        private final String key;
        private final ItemStack is; // Default
        private final YamlConfiguration config = ConfigManager.getConfig().config;

        ConfigKey(String s) {
            this(s, null);
        }

        ConfigKey(String s, ItemStack is) {
            this.key = s;
            this.is = is;
        }

        public String getString() {
            return config.getString(key);
        }

        public boolean getBoolean() {
            return config.getBoolean(key);
        }

        public int getInt() {
            return config.getInt(key);
        }

        public ItemStack getItemStack() {
            XMaterial mat = XMaterial.matchXMaterial(getString()).get();
            return mat != null ? mat.parseItem() : (is != null ? is : new ItemStack(Material.AIR)); // Air to prevent nullpointer for non-material config types
        }

    }

}
