package nl.thieme.tp;

import nl.thieme.tp.commands.TPCmd;
import nl.thieme.tp.essentials.EssentialResolverLoader;
import nl.thieme.tp.events.*;
import nl.thieme.tp.managers.ConfigManager;
import nl.thieme.tp.models.Present;
import nl.thieme.tp.utils.InvUtil;
import nl.thieme.tp.utils.MsgUtil;
import nl.thieme.tp.models.TPermission;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
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
    public static PluginDescriptionFile pluginFile;
    private boolean hasEssentials = false;

    public void onEnable() {
        INSTANCE = this;
        pluginFile = getDescription();
        LOGGER = getLogger();
        if (getDataFolder().exists() && new File(getDataFolder(), "debug").exists()) DEBUG = true;

        if (DEBUG) LOGGER.info("Found server version: " + SERVER_VERSION);
        loading(pluginFile.getFullName());
        if (DEBUG) loading("configs");
        new ConfigManager();
        MsgUtil.loadVariables();
        if (DEBUG) doneLoading("configs");

        registerEvents();
        registerCommands();
        registerPermissions();

        if (getServer().getPluginManager().getPlugin("Essentials") != null) hasEssentials = true;
        if (hasEssentials) EssentialResolverLoader.addItemsToEssentials(this);

        doneLoading("");
    }

    private void registerPermissions() {
        for(TPermission tp : TPermission.values()) {
            Permission perm = new Permission(tp.getPermission());
            Bukkit.getPluginManager().addPermission(perm);
        }
    }

    public void onDisable() {
        if (hasEssentials) EssentialResolverLoader.removeItems(this);
        InvUtil.removeAllBackups();
        if (ConfigManager.getPresentConfig() != null) {
            for (Present present : ConfigManager.getPresentConfig().getPresents()) {
                present.removeRecipe();
            }
        }

    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new ChatEvent(), this);
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


    private void loading(String s) {
        LOGGER.info("Loading " + s + "...");
    }

    private void doneLoading(String s) {
        LOGGER.info("Done loading" + (s.length() == 0 ? "" : " " + s) + "!");
    }

}
