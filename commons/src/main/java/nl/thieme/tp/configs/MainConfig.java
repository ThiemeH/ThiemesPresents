package nl.thieme.tp.configs;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import nl.thieme.tp.ThiemesPresents;
import nl.thieme.tp.models.FileConfig;
import nl.thieme.tp.models.FullSound;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.List;

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
        ALLOW_STORAGE_WRAPPING("allow-wrapping-storage-items"),
        PLACE_ON_HEAD("presents-can-be-placed-on-head"),
        OPEN_SOUND("open-sound"),
        WRAP_SOUND("wrap-sound"),
        DISABLED_WORLDS("disabled-worlds");

        private final String key;
        private final ItemStack is; // Default
        private final YamlConfiguration config = ThiemesPresents.getConfigManager().getConfig().config;

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

        public List<String> getStringList() {
            return config.getStringList(key);
        }

        public FullSound getFullSound() {
            if (!config.isConfigurationSection(key)) return null;
            ConfigurationSection section = config.getConfigurationSection(key);
            String sound = section.getString("sound");
            double pitch = section.getDouble("pitch");
            double volume = section.getDouble("volume");
            return new FullSound(XSound.valueOf(sound), (float) volume, (float) pitch);
        }

        public ItemStack getItemStack() {
            XMaterial mat = XMaterial.matchXMaterial(getString()).get();
            return mat != null ? mat.parseItem() : (is != null ? is : new ItemStack(Material.AIR)); // Air to prevent nullpointer for non-material config types
        }
    }
}
