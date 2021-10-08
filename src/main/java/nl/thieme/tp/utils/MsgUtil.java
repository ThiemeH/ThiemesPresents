package nl.thieme.tp.utils;

import nl.thieme.tp.configs.MessageConfig;
import org.bukkit.command.CommandSender;

public class MsgUtil {


    public static void sendMessage(CommandSender s, MessageConfig.MessageKey key) {
        s.sendMessage(replaceColors(MessageConfig.MessageKey.PREFIX.get() + key.get()));
    }

    public static String replaceColors(String s) {
        return s.replaceAll("&", "§");
    }
}
