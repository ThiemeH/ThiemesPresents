package nl.thieme.tp.commands;

import nl.thieme.tp.models.SubCommand;
import nl.thieme.tp.models.TPermission;
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
        commandSender.sendMessage("§a--------------");
        for(SubCommand sub : subCommands) {
            commandSender.sendMessage("§6" + sub.getMainCommand() + " §a- §6" + sub.getDescription());
        }
        commandSender.sendMessage("§a--------------");

        return true;
    }
}
