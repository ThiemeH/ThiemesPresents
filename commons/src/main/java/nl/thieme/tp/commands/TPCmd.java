package nl.thieme.tp.commands;

import nl.thieme.tp.models.SubCommand;
import nl.thieme.tp.models.TPermission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class TPCmd implements CommandExecutor, TabCompleter {

    ArrayList<SubCommand> subCommands = new ArrayList<>();

    public TPCmd() {
        subCommands.add(new TPSReloadCmd("reload", TPermission.NP_CMD_RELOAD,"Reload the plugin", "rl"));
        subCommands.add(new TPSInsideCmd("lookinside", TPermission.NP_CMD_INSIDE,"Look inside the gift", "li"));
        subCommands.add(new TPSAboutCmd("about", null, "Display plugin info"));
        subCommands.add(new TPSHelpCmd(subCommands, "help", null,"Displays this message"));
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        for (SubCommand sc : subCommands) {
            if (args.length >= 1 && sc.getCommands().contains(args[0])) {
                if (TPermission.hasPermission(commandSender, sc.getPermission())) {
                    return sc.onExecute(commandSender, command, s, args);
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length == 1) {
            ArrayList<String> tabCompletion = new ArrayList<>();
            for (SubCommand sc : subCommands) {
                if (TPermission.hasPermission(commandSender, sc.getPermission())) tabCompletion.addAll(sc.getCommands());
            }
            return tabCompletion;

        }
        return null;
    }
}
