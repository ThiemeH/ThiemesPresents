package nl.thieme.tp.essentials;

import com.earth2me.essentials.Essentials;
import nl.thieme.tp.Main;
import nl.thieme.tp.managers.ConfigManager;
import org.bukkit.plugin.Plugin;

public class EssentialResolverLoader {

    private static final String presentItemResolver = "PresentResolver";
    private static Plugin essentials;

    public static void addItemsToEssentials(Main main) {
        if (main.getServer().getPluginManager().getPlugin("Essentials") == null) {
            if (main.DEBUG) main.LOGGER.info("Essentials not installed!");
            return;
        }
        if (main.DEBUG) main.LOGGER.info("Found essentials!");
        essentials = main.getServer().getPluginManager().getPlugin("Essentials");
        PresentItemResolver resolver = new PresentItemResolver(ConfigManager.getPresentConfig().getPresents());
        try {
            ((Essentials) essentials).getItemDb().registerResolver(main, presentItemResolver, resolver);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void removeItems(Main main) {
        if (essentials != null) {
            try {
                ((Essentials) essentials).getItemDb().unregisterResolver(main, presentItemResolver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
