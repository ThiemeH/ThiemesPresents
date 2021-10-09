package nl.thieme.tp.utils;

import nl.thieme.tp.configs.MainConfig;
import nl.thieme.tp.configs.MessageConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

public class MsgUtil {

    private static HashMap<String, String> varMap = new HashMap<>();
    public static final String varBracket = "%";
    public static final String fromKey = genVar("FROM");
    public static final String toKey = genVar("TO");

    public static void loadVariables() {
        varMap.put("SECONDS", String.valueOf(MainConfig.ConfigKey.TIMEOUT_SECONDS.getInt()));
        varMap.put("KEYWORD", MessageConfig.MessageKey.CANCEL_KEYWORD.get());
    }

    public static void sendMessage(CommandSender s, MessageConfig.MessageKey key, boolean prefix) {
        if (key.get().length() > 0) {
            s.sendMessage(replaceColorsAndVariables((prefix ? MessageConfig.MessageKey.PREFIX.get() : "") + key.get()));
        }
    }

    public static void sendMessage(CommandSender s, MessageConfig.MessageKey key) {
        sendMessage(s, key, true);
    }

    public static String replaceColors(String s) {
        return s.replaceAll("&", "§");
    }

    public static String replaceVariables(String s) {
        for(String key : varMap.keySet()) {
            String fullKey = genVar(key);
            if(s.contains(fullKey)) {
                s = s.replaceAll(fullKey, varMap.get(key));
            }
        }
        return s;
    }

    public static String replaceColorsAndVariables(String s) {
        return replaceColors(replaceVariables(s));
    }

    private static String genVar(String s) {
        return varBracket + s + varBracket;
    }
}
