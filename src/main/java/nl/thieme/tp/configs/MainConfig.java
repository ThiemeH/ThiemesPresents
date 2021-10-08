package nl.thieme.tp.configs;

import nl.thieme.tp.managers.ConfigManager;
import nl.thieme.tp.models.FileConfig;
import org.bukkit.configuration.file.YamlConfiguration;

public class MainConfig extends FileConfig {

    public MainConfig(String name) {
        super(name);
    }


    public enum ConfigKey {
        RENAME_ANVIL("can-rename-in-anvil"),
        STORAGE_WRAPPING_LIMIT("storage-item-wrapping-limit"),
        CAN_SIGN("can-sign"),
        ALLOW_STORAGE_WRAPPING("allow-wrapping-storage-items");

        private String key;
        private YamlConfiguration config = ConfigManager.getConfig().config;

        ConfigKey(String s) {
            this.key = s;
        }

        public String getString() {
            return config.getString(key);
        }

        public boolean getBoolean() {
            return config.getBoolean(key);
        }

    }

}
