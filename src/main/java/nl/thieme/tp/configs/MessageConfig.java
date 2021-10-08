package nl.thieme.tp.configs;

import nl.thieme.tp.managers.ConfigManager;
import nl.thieme.tp.models.FileConfig;

public class MessageConfig extends FileConfig {

    public MessageConfig(String name) {
        super(name);
    }

    public String get(String key) {
        return config.getString(key);
    }

    public enum MessageKey {
        PREFIX("prefix"),
        RELOADING("reloading"),
        NO_ANVIL_RENAME("no-anvil-rename"),
        DONE_RELOADING("done-reloading");

        private String key;

        MessageKey(String s) {
            this.key = s;
        }

        public String get() {
            return ConfigManager.getMsgConfig().get(key);
        }
    }
}
