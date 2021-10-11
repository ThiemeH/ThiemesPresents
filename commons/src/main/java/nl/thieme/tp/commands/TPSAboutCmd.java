package nl.thieme.tp.commands;

import nl.thieme.tp.ThiemesPresents;
import nl.thieme.tp.extra.Constants;
import nl.thieme.tp.models.SubCommand;
import nl.thieme.tp.models.TPermission;
import nl.thieme.tp.utils.MsgUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class TPSAboutCmd extends SubCommand {

    public TPSAboutCmd(String s, TPermission permission, String infoMessage, String... alias) {
        super(s, permission, infoMessage, alias);
    }

    @Override
    public boolean onExecute(CommandSender commandSender, Command command, String s, String[] args) {
        commandSender.sendMessage(MsgUtil.replaceColorsAndVariables(Constants.dividerTop));
        commandSender.sendMessage(MsgUtil.replaceColorsAndVariables(Constants.secondColor + "  Version " + Constants.mainColor + "- " + Constants.secondColor + ThiemesPresents.pluginFile.getVersion()));
        commandSender.sendMessage(MsgUtil.replaceColorsAndVariables(Constants.secondColor + "  Author " + Constants.mainColor + "- " + Constants.secondColor + ThiemesPresents.pluginFile.getAuthors().get(0)));
        commandSender.sendMessage(MsgUtil.replaceColorsAndVariables(Constants.dividerBottom));
        return true;
    }
}
