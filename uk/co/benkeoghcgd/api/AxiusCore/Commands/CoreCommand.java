package uk.co.benkeoghcgd.api.AxiusCore.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusCommand;
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlugin;
import uk.co.benkeoghcgd.api.AxiusCore.AxiusCore;
import uk.co.benkeoghcgd.api.AxiusCore.Exceptions.CoreSelfUpdateException;
import uk.co.benkeoghcgd.api.AxiusCore.Exceptions.MissingDependException;
import uk.co.benkeoghcgd.api.AxiusCore.GUIs.CoreGUI;
import uk.co.benkeoghcgd.api.AxiusCore.Utils.Logging;

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
    public boolean onCommand(CommandSender sndr, Command command, String s, String[] args) {
        if(args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "resolve":
                    if(args.length == 2) {
                        String[] parts = args[1].split(":");
                        AxiusPlugin correct = null;
                        for (AxiusPlugin plg : AxiusCore.registeredPlugins) {
                            if(plg.getName().equalsIgnoreCase(parts[0])) correct = plg;
                        }
                        if(correct == null) {
                            sndr.sendMessage(getUsage());
                            return false;
                        }

                        int errorID = Integer.parseInt(parts[1]);
                        if(errorID < correct.errors.size()) {
                            correct.errors.remove(errorID);
                            sndr.sendMessage(core.PREFIX + "Successfully resolved error. (" + args[1] + ")");
                            return true;
                        }
                    }
                    sndr.sendMessage(getUsage());
                    return false;

                case "print":
                    if(args.length == 2) {
                        String[] parts = args[1].split(":");
                        AxiusPlugin correct = null;
                        for (AxiusPlugin plg : AxiusCore.registeredPlugins) {
                            if(plg.getName().equalsIgnoreCase(parts[0])) correct = plg;
                        }
                        if(correct == null) {
                            sndr.sendMessage(getUsage());
                            return false;
                        }

                        int errorID = Integer.parseInt(parts[1]);
                        if(errorID < correct.errors.size()) {
                            correct.errors.get(errorID).printStackTrace();
                            sndr.sendMessage(core.PREFIX + "Successfully printed error stack to console. (" + args[1] + ")");
                            return true;
                        }
                    }
                    sndr.sendMessage(getUsage());
                    return false;

                case "debug":
                    for (AxiusPlugin plg : AxiusCore.registeredPlugins) {
                        plg.errors.add(new MissingDependException("Fake Error"));
                    }
                    break;

                default:
                    return false;
            }
        }
        else {
            CoreGUI cgui = new CoreGUI(core);
            cgui.show((Player)sndr);
        }

        return true;
    }
}
