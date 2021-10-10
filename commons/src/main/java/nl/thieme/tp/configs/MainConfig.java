package nl.thieme.tp.configs;

import nl.thieme.tp.Main;
import nl.thieme.tp.managers.ConfigManager;
import nl.thieme.tp.models.FileConfig;
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
        PENDING_MATERIAL("pending-material", Main.WRAPPER.getOrangeConcreteItemStack()),
        CONFIRM_MATERIAL("confirm-material", Main.WRAPPER.getLimeConcreteItemStack()),
        BLOCKED_SLOT_MATERIAL("blocked-slot-material", Main.WRAPPER.getGrayStainedGlass()),
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
            Material mat = Material.valueOf(getString());
            ItemStack stack = new ItemStack(mat);
            return mat != null ? stack : (is != null ? is : new ItemStack(Material.AIR)); // Air to prevent nullpointer for non-material config types
        }

    }

}
