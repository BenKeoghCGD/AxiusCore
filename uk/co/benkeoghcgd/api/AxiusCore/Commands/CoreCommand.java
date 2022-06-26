package uk.co.benkeoghcgd.api.AxiusCore.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusCommand;
import uk.co.benkeoghcgd.api.AxiusCore.AxiusCore;
import uk.co.benkeoghcgd.api.AxiusCore.GUIs.CoreGUI;

public class CoreCommand extends AxiusCommand {
    AxiusCore core;

    public CoreCommand(AxiusCore instance) {
        super(instance, false,
                "core",
                "default core command",
                instance.PREFIX + "How did you mess this up?",
                "c", "co");
        setPermission("axiuscore.admin");
        core = instance;
    }

    @Override
    public boolean onCommand(CommandSender sndr, Command command, String s, String[] strings) {
        CoreGUI cgui = new CoreGUI(core);
        cgui.show((Player)sndr);

        return true;
    }
}
