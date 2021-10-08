package nl.thieme.tp.commands;

import nl.thieme.tp.models.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class TPCmd implements CommandExecutor, TabCompleter {

    ArrayList<SubCommand> subCommands = new ArrayList<>();

    public TPCmd() {
        subCommands.add(new TPSReloadCmd("reload", "reload", "Reload the plugin", "rl"));
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        for (SubCommand sc : subCommands) {
            if (args.length >= 1 && sc.getCommands().contains(args[0])) {
                if (sc.hasPermission(commandSender)) {
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
                if (sc.hasPermission(commandSender)) tabCompletion.addAll(sc.getCommands());
            }
            return tabCompletion;

        }
        return null;
    }
}
