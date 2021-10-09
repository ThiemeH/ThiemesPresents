package nl.thieme.tp.commands;

import nl.thieme.tp.Main;
import nl.thieme.tp.models.SubCommand;
import nl.thieme.tp.models.TPermission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class TPSAboutCmd extends SubCommand {

    public TPSAboutCmd(String s, TPermission permission, String infoMessage, String... alias) {
        super(s, permission, infoMessage, alias);
    }

    @Override
    public boolean onExecute(CommandSender commandSender, Command command, String s, String[] args) {
        commandSender.sendMessage("§a--------------");
        commandSender.sendMessage("§6Version §a- §6" + Main.pluginFile.getVersion());
        commandSender.sendMessage("§6Author §a- §6" + Main.pluginFile.getAuthors().get(0));
        commandSender.sendMessage("§a--------------");
        return true;
    }
}
