package uk.co.benkeoghcgd.api.AxiusCore.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import uk.co.benkeoghcgd.api.AxiusCore.AxiusCore;
import uk.co.benkeoghcgd.api.AxiusCore.GUIs.CoreGUI;

public class CoreCommand implements CommandExecutor {

    AxiusCore core;

    public CoreCommand(AxiusCore instance) {
        core = instance;
    }

    @Override
    public boolean onCommand(CommandSender sndr, Command command, String s, String[] strings) {
        if(sndr instanceof ConsoleCommandSender) {
            sndr.sendMessage("§cOnly players can run core commands.");
            return false;
        }

        if(!sndr.hasPermission("axiuscore.core")) {
            sndr.sendMessage(AxiusCore.PREFIX + "You don't have permission to run this command.");
            return false;
        }

        CoreGUI cgui = new CoreGUI(core);
        cgui.show((Player)sndr);

        return true;
    }
}
