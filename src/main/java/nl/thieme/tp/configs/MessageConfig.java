package nl.thieme.tp.configs;

import nl.thieme.tp.managers.ConfigManager;
import nl.thieme.tp.models.FileConfig;
import nl.thieme.tp.utils.MsgUtil;

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
        PICK_PRESENT_TITLE("pick-present-title"),
        CLICK_CONFIRM("click-to-confirm"),
        CHOOSE_ITEM("choose-an-item"),
        SIGN_FROM("lore-from"),
        SIGN_TO("lore-to"),
        DONE_RELOADING("done-reloading");

        private String key;

        MessageKey(String s) {
            this.key = s;
        }

        public String get() {
            return MsgUtil.replaceColors(ConfigManager.getMsgConfig().get(key));
        }
    }
}
