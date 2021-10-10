package nl.thieme.tp.commands;

import nl.thieme.tp.configs.MessageConfig;
import nl.thieme.tp.inventories.PeekInventory;
import nl.thieme.tp.models.PresentNBT;
import nl.thieme.tp.models.SubCommand;
import nl.thieme.tp.models.TPermission;
import nl.thieme.tp.utils.MsgUtil;
import nl.thieme.tp.utils.PresentUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TPSInsideCmd extends SubCommand {

    public TPSInsideCmd(String s, TPermission permission, String infoMessage, String... alias) {
        super(s, permission, infoMessage, alias);
    }

    @Override
    public boolean onExecute(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            Player p = (Player) commandSender;

            if (!PresentUtil.isPresentItemStack(p.getInventory().getItemInMainHand())) {
                MsgUtil.sendMessage(commandSender, MessageConfig.MessageKey.NOT_A_PRESENT);
                return true;
            }

            PresentNBT presentNBT = PresentUtil.getPresentNBT(p.getInventory().getItemInMainHand());
            if (!presentNBT.hasPresent()) {
                MsgUtil.sendMessage(commandSender, MessageConfig.MessageKey.NO_PRESENT);
                return true;
            }
            p.openInventory(new PeekInventory(presentNBT).getInventory());

        }
        return true;
    }
}
