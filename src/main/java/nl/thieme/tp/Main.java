package nl.thieme.tp;

import com.earth2me.essentials.Essentials;
import nl.thieme.tp.commands.TPCmd;
import nl.thieme.tp.events.*;
import nl.thieme.tp.managers.ConfigManager;
import nl.thieme.tp.models.Present;
import nl.thieme.tp.resolvers.PresentItemResolver;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public class Main extends JavaPlugin {

    public static final String SERVER_VERSION = Bukkit.getBukkitVersion().split("-")[0];
    public static final String pluginId = "thiemespresents";
    public static Main INSTANCE;
    public static Logger LOGGER;
    public static boolean DEBUG = false;
    private final String presentItemResolver = "PresentResolver";
    private PluginDescriptionFile pluginFile;
    private Plugin essentials;

    public void onEnable() {
        INSTANCE = this;
        pluginFile = getDescription();
        LOGGER = getLogger();
        if (getDataFolder().exists() && new File(getDataFolder(), "debug").exists()) DEBUG = true;

        if (DEBUG) LOGGER.info("Found server version: " + SERVER_VERSION);
        loading(pluginFile.getFullName());
        if (DEBUG) loading("configs");
        new ConfigManager();
        if (DEBUG) doneLoading("configs");

        registerEvents();
        registerCommands();

        addItemsToEssentials();
        doneLoading("");
    }

    public void onDisable() {
        if (essentials != null) {
            try {
                ((Essentials) essentials).getItemDb().unregisterResolver(this, presentItemResolver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (ConfigManager.getPresentConfig() != null) {
            for (Present present : ConfigManager.getPresentConfig().getPresents()) {
                present.removeRecipe();
            }
        }
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new InvClickEvent(), this);
        Bukkit.getPluginManager().registerEvents(new InvCloseEvent(), this);
        Bukkit.getPluginManager().registerEvents(new InvOpenEvent(), this);
        Bukkit.getPluginManager().registerEvents(new InteractEvent(), this);
        Bukkit.getPluginManager().registerEvents(new BlockPlaceEvent(), this);
    }

    private void registerCommands() {
        TPCmd tpCommand = new TPCmd();
        getCommand(pluginId).setExecutor(tpCommand);
        getCommand(pluginId).setTabCompleter(tpCommand);
    }

    private void addItemsToEssentials() {
        if (getServer().getPluginManager().getPlugin("Essentials") == null) {
            if (DEBUG) LOGGER.info("Essentials not installed!");
            return;
        }
        if (DEBUG) LOGGER.info("Found essentials!");
        essentials = getServer().getPluginManager().getPlugin("Essentials");
        PresentItemResolver resolver = new PresentItemResolver(ConfigManager.getPresentConfig().getPresents());
        try {
            ((Essentials) essentials).getItemDb().registerResolver(this, presentItemResolver, resolver);
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
