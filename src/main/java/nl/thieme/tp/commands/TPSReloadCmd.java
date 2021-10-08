package nl.thieme.tp.commands;

import nl.thieme.tp.configs.MessageConfig;
import nl.thieme.tp.managers.ConfigManager;
import nl.thieme.tp.models.SubCommand;
import nl.thieme.tp.utils.MsgUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class TPSReloadCmd extends SubCommand {

    public TPSReloadCmd(String s, String permission, String infoMessage, String... alias) {
        super(s, permission, infoMessage, alias);
    }

    @Override
    public boolean onExecute(CommandSender commandSender, Command command, String s, String[] args) {
        MsgUtil.sendMessage(commandSender, MessageConfig.MessageKey.RELOADING);
        ConfigManager.reloadAll();
        MsgUtil.sendMessage(commandSender, MessageConfig.MessageKey.DONE_RELOADING);
        return true;
    }

}
