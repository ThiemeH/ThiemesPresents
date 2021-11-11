package nl.thieme.tp;

import nl.thieme.tp.commands.TPCmd;
import nl.thieme.tp.essentials.EssentialResolverLoader;
import nl.thieme.tp.events.*;
import nl.thieme.tp.extra.Constants;
import nl.thieme.tp.managers.ConfigManager;
import nl.thieme.tp.managers.PresentManager;
import nl.thieme.tp.models.ITPWrapper;
import nl.thieme.tp.models.Present;
import nl.thieme.tp.models.PresentInventory;
import nl.thieme.tp.models.TPermission;
import nl.thieme.tp.utils.InvUtil;
import nl.thieme.tp.utils.MsgUtil;
import nl.thieme.tp.wrappers.TPWrapper_12;
import nl.thieme.tp.wrappers.TPWrapper_Up;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public class ThiemesPresents extends JavaPlugin {

    public static int SERVER_VERSION;
    public static ITPWrapper WRAPPER;
    public static ThiemesPresents INSTANCE;
    public static Logger LOGGER;
    public static boolean DEBUG = false;
    public static PluginDescriptionFile pluginFile;
    private static ConfigManager configManager;
    private static PresentManager presentManager;
    public boolean hasEssentials = false;
    public static boolean isFirstRun = true;

    public static ConfigManager getConfigManager() {
        return configManager;
    }

    public static PresentManager getPresentManager() {
        return presentManager;
    }

    public void onEnable() {
        INSTANCE = this;
        pluginFile = getDescription();
        LOGGER = getLogger();
        SERVER_VERSION = Integer.parseInt(Bukkit.getBukkitVersion().split("-")[0].split("\\.")[1]);
        if (getDataFolder().exists() && new File(getDataFolder(), "debug").exists()) DEBUG = true;
        MsgUtil.debugInfo("Found server version: " + SERVER_VERSION);
        WRAPPER = SERVER_VERSION > 12 ? new TPWrapper_Up() : new TPWrapper_12();

        loading(pluginFile.getFullName());
        if (DEBUG) loading("configs");
        presentManager = new PresentManager();
        configManager = new ConfigManager();
        configManager.loadConfigs();
        PresentInventory.initConfigItemStacks();
        MsgUtil.loadVariables();
        if (DEBUG) doneLoading("configs");

        // Reloading via command shouldn't double register events
        if(isFirstRun) {
            registerEvents();
            registerCommands();
            registerPermissions();
        }

        // Essentials
        if (getServer().getPluginManager().getPlugin("Essentials") != null) hasEssentials = true;
        if (hasEssentials) EssentialResolverLoader.addItemsToEssentials(this);

        doneLoading("");
        isFirstRun = false;
    }

    private void registerPermissions() {
        for (TPermission tp : TPermission.values()) {
            Permission perm = new Permission(tp.getPermission());
            if (Bukkit.getPluginManager().getPermission(tp.getPermission()) == null) {
                Bukkit.getPluginManager().addPermission(perm);
            }
        }
    }

    public void onDisable() {
        if (hasEssentials) EssentialResolverLoader.removeItems(this);
        InvUtil.removeAllBackups();
        if (getConfigManager().getPresentConfig() != null) {
            for (Present present : getPresentManager().getPresents()) {
                present.removeRecipe();
            }
        }
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new ChatEvent(), this);
        Bukkit.getPluginManager().registerEvents(new CraftEvent(), this);
        Bukkit.getPluginManager().registerEvents(new ItemDropEvent(), this);
        Bukkit.getPluginManager().registerEvents(new InvClickEvent(), this);
        Bukkit.getPluginManager().registerEvents(new InvCloseEvent(), this);
        Bukkit.getPluginManager().registerEvents(new InvOpenEvent(), this);
        Bukkit.getPluginManager().registerEvents(new InteractEvent(), this);
        Bukkit.getPluginManager().registerEvents(new BlockPlaceEvent(), this);
    }

    private void registerCommands() {
        TPCmd tpCommand = new TPCmd();
        getCommand(Constants.pluginId).setExecutor(tpCommand);
        getCommand(Constants.pluginId).setTabCompleter(tpCommand);
    }

    private void loading(String s) {
        LOGGER.info("Loading " + s + "...");
    }

    private void doneLoading(String s) {
        LOGGER.info("Done loading" + (s.length() == 0 ? "" : " " + s) + "!");
    }
}
