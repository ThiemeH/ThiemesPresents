package nl.thieme.tp.managers;

import nl.thieme.tp.Main;
import nl.thieme.tp.configs.MainConfig;
import nl.thieme.tp.configs.MessageConfig;
import nl.thieme.tp.configs.PresentConfig;
import nl.thieme.tp.models.FileConfig;

import java.util.ArrayList;

public class ConfigManager {

    private static PresentConfig presentConfig;
    private static MessageConfig messageConfig;
    private static MainConfig mainConfig;

    public ConfigManager() {
        mainConfig = new MainConfig("config");
        presentConfig = new PresentConfig("presents");
        messageConfig = new MessageConfig("messages");
    }

    public static PresentConfig getPresentConfig() {
        return presentConfig;
    }
    public static MessageConfig getMsgConfig() {
        return messageConfig;
    }

    public static MainConfig getConfig() {
        return mainConfig;
    }

    public static void reloadAll() {
        Main.INSTANCE.onDisable();
        Main.INSTANCE.onEnable();
    }


}
