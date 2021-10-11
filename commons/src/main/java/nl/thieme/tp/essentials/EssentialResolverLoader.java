package nl.thieme.tp.essentials;

import com.earth2me.essentials.Essentials;
import nl.thieme.tp.ThiemesPresents;
import nl.thieme.tp.utils.MsgUtil;
import org.bukkit.plugin.Plugin;

public class EssentialResolverLoader {

    private static final String presentItemResolver = "PresentResolver";
    private static Plugin essentials;

    public static void addItemsToEssentials(ThiemesPresents thiemesPresents) {
        if (thiemesPresents.getServer().getPluginManager().getPlugin("Essentials") == null) {
            MsgUtil.debugInfo("Essentials not installed!");
            return;
        }
        MsgUtil.debugInfo("Found essentials!");
        essentials = thiemesPresents.getServer().getPluginManager().getPlugin("Essentials");
        PresentItemResolver resolver = new PresentItemResolver(ThiemesPresents.getPresentManager().getPresents());
        try {
            ((Essentials) essentials).getItemDb().registerResolver(thiemesPresents, presentItemResolver, resolver);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void removeItems(ThiemesPresents thiemesPresents) {
        if (essentials != null) {
            try {
                ((Essentials) essentials).getItemDb().unregisterResolver(thiemesPresents, presentItemResolver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
