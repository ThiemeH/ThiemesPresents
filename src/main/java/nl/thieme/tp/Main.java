package nl.thieme.tp;

import com.earth2me.essentials.Essentials;
import net.ess3.api.IItemDb;
import nl.thieme.tp.configs.PresentConfig;
import nl.thieme.tp.managers.ConfigManager;
import nl.thieme.tp.models.Present;
import nl.thieme.tp.resolvers.PresentItemResolver;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class Main extends JavaPlugin {

    public static Main INSTANCE;
    public static Logger LOGGER;
    public static boolean DEBUG = true;
    public static final String SERVER_VERSION = Bukkit.getBukkitVersion().split("-")[0];
    private PluginDescriptionFile pluginFile;
    private final String presentItemResolver = "PresentResolver";
    private Plugin essentials;

    public void onEnable() {
        INSTANCE = this;
        pluginFile = getDescription();
        LOGGER = Logger.getLogger(pluginFile.getName());
        if(DEBUG) LOGGER.info("Found server version: " + SERVER_VERSION);

        loading(pluginFile.getFullName());
        if(DEBUG) LOGGER.info("Loading configs...");
        new ConfigManager();
        if(DEBUG) LOGGER.info("Done loading configs!");

        addItemsToEssentials();
        doneLoading("");
    }

    public void onDisable() {
        if(essentials != null) {
            try {
                ((Essentials)essentials).getItemDb().unregisterResolver(this, presentItemResolver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(Present present : ConfigManager.getPresentConfig().getPresents()) {
            present.removeRecipe();
        }
    }


    private void addItemsToEssentials() {
        if(getServer().getPluginManager().getPlugin("Essentials")==null) {
            if(DEBUG) LOGGER.info("Essentials not installed!");
            return;
        }
        if(DEBUG) LOGGER.info("Found essentials!");
        essentials = getServer().getPluginManager().getPlugin("Essentials");
        PresentItemResolver resolver = new PresentItemResolver(ConfigManager.getPresentConfig().getPresents());
        try {
            ((Essentials)essentials).getItemDb().registerResolver(this, presentItemResolver, resolver);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loading(String s) {
        LOGGER.info("Loading " + s + "...");
    }

    private void doneLoading(String s) {
        LOGGER.info("Done loading" + (s.length() == 0 ? "" : " " + s) + "!");
    }

}
