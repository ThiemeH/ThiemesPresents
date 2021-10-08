package nl.thieme.tp;

import nl.thieme.tp.managers.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class Main extends JavaPlugin {

    public static Main INSTANCE;
    public static Logger LOGGER;
    public static boolean DEBUG = true;
    private PluginDescriptionFile pluginFile;

    public void onEnable() {
        INSTANCE = this;
        pluginFile = getDescription();
        LOGGER = Bukkit.getLogger();

        loading(pluginFile.getFullName());
        if(DEBUG) LOGGER.info("Loading configs...");
        new ConfigManager();
        if(DEBUG) LOGGER.info("Done loading configs!");

        doneLoading("");
    }

    public void onDisable() {

    }

    private void loading(String s) {
        LOGGER.info("Loading " + s + "...");
    }

    private void doneLoading(String s) {
        LOGGER.info("Done loading" + (s.length() == 0 ? "" : " " + s) + "!");
    }

}
