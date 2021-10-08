package nl.thieme.tp.managers;

import nl.thieme.tp.configs.PresentConfig;
import nl.thieme.tp.models.FileConfig;

public class ConfigManager {

    private final String presentConfigName = "presents";
    private static PresentConfig presentConfig;

    public ConfigManager() {
        presentConfig = new PresentConfig(presentConfigName);
    }

    public static PresentConfig getPresentConfig() {
        return presentConfig;
    }


}
