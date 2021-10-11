package nl.thieme.tp.models;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class SubCommand {

    private final String infoMessage;
    private final String mainCommand;
    private final TPermission permission;
    private List<String> aliases;

    public SubCommand(String s, TPermission permission, String infoMessage, String... alias) {
        this.mainCommand = s;
        this.infoMessage = infoMessage;
        this.permission = permission;
        if (alias != null) aliases = Arrays.asList(alias);
    }

    public boolean onExecute(CommandSender commandSender, Command command, String s, String[] args) {
        return false;
    }

    public List<String> getCommands() {
        List<String> copy = new ArrayList<>();
        if (aliases != null) copy.addAll(aliases);
        copy.add(mainCommand);
        return copy;
    }

    public TPermission getPermission() {
        return permission;
    }

    public String getMainCommand() {
        return mainCommand;
    }

    public String getDescription() {
        return infoMessage;
    }
}
