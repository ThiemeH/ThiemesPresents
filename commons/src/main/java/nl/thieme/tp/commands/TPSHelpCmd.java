package nl.thieme.tp.commands;

import nl.thieme.tp.extra.Constants;
import nl.thieme.tp.models.SubCommand;
import nl.thieme.tp.models.TPermission;
import nl.thieme.tp.utils.MsgUtil;
import nl.thieme.tp.utils.StringUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public class TPSHelpCmd extends SubCommand {

    ArrayList<SubCommand> subCommands;

    public TPSHelpCmd(ArrayList<SubCommand> subCommands, String s, TPermission permission, String infoMessage, String... alias) {
        super(s, permission, infoMessage, alias);
        this.subCommands = subCommands;
    }

    @Override
    public boolean onExecute(CommandSender commandSender, Command command, String s, String[] args) {
        commandSender.sendMessage(MsgUtil.replaceColorsAndVariables(Constants.dividerTop));
        int longestCommand = -1;
        for(SubCommand sub : subCommands) {
           if(sub.getMainCommand().length() < longestCommand) continue;
           longestCommand = sub.getMainCommand().length();
        }

        for(SubCommand sub : subCommands) {
            commandSender.sendMessage(MsgUtil.replaceColorsAndVariables(Constants.secondColor + "  " +StringUtil.fillSpace(sub.getMainCommand(), longestCommand+2) + " " + Constants.mainColor + "- " + StringUtil.repeatSpace(2) + Constants.secondColor + sub.getDescription()));
        }
        commandSender.sendMessage(MsgUtil.replaceColorsAndVariables(Constants.dividerBottom));

        return true;
    }

}
