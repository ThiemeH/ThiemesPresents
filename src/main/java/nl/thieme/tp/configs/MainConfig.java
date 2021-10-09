package nl.thieme.tp.configs;

import nl.thieme.tp.managers.ConfigManager;
import nl.thieme.tp.models.FileConfig;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

public class MainConfig extends FileConfig {

    public MainConfig(String name) {
        super(name);
    }


    public enum ConfigKey {
        RENAME_ANVIL("can-rename-in-anvil"),
        STORAGE_WRAPPING_LIMIT("storage-item-wrapping-limit"),
        PENDING_MATERIAL("pending-material", Material.ORANGE_CONCRETE),
        CONFIRM_MATERIAL("confirm-material", Material.LIME_CONCRETE),
        BLOCKED_SLOT_MATERIAL("blocked-slot-material", Material.GRAY_STAINED_GLASS_PANE),
        CAN_SIGN("can-sign"),
        TIMEOUT_SECONDS("signing-timeout-seconds"),
        SIGN_CHARACTER_LIMIT("sign-character-limit"),
        ALLOW_STORAGE_WRAPPING("allow-wrapping-storage-items");

        private final String key;
        private final Material m; // Default
        private final YamlConfiguration config = ConfigManager.getConfig().config;

        ConfigKey(String s) {
            this(s, null);
        }

        ConfigKey(String s, Material m) {
            this.key = s;
            this.m = m;
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

        public Material getMaterial() {
            Material mat = Material.valueOf(getString());
            return mat != null ? mat : (m != null ? m : Material.AIR); // Air to prevent nullpointer for non-material config types
        }

    }

}
