package nl.thieme.tp.models;

import nl.thieme.tp.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class SubCommand {

    public String infoMessage;
    public List<String> aliases;
    public String mainCommand;
    private final String permission;

    public SubCommand(String s, String permission, String infoMessage, String... alias) {
        this.mainCommand = s;
        this.infoMessage = infoMessage;
        this.permission = permission;
        if (alias != null) aliases = Arrays.asList(alias);
    }

    public boolean onExecute(CommandSender commandSender, Command command, String s, String[] args) {
        return false;
    }

    public boolean hasPermission(CommandSender p) {
        return permission == null || p.hasPermission(Main.pluginId + permission);
    }

    public List<String> getCommands() {
        List<String> copy = new ArrayList<>();
        if (aliases != null) copy.addAll(aliases);
        copy.add(mainCommand);
        return copy;
    }


}
