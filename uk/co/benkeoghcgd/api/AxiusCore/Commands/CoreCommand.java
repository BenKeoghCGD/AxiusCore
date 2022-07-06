package uk.co.benkeoghcgd.api.AxiusCore.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusCommand;
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlugin;
import uk.co.benkeoghcgd.api.AxiusCore.AxiusCore;
import uk.co.benkeoghcgd.api.AxiusCore.GUIs.CoreGUI;

public class CoreCommand extends AxiusCommand {
    AxiusCore core;
    private enum ErrorActions { PRINT, RESOLVE, NULL }

    public CoreCommand(AxiusCore instance) {
        super(instance, false,
                "core",
                "default core command",
                instance.getNameFormatted() + "How did you mess this up?",
                "c", "co");
        setPermission("axiuscore.admin");
        core = instance;
    }

    @Override
    public boolean onCommand(CommandSender sndr, Command command, String s, String[] args) {
        if(args.length > 0) {
            ErrorActions action = switch (args[0].toLowerCase()) {
                case "resolve" -> ErrorActions.RESOLVE;
                case "print" -> ErrorActions.PRINT;
                default -> ErrorActions.NULL;
            };

            if(args.length == 2) {
                String[] parts = args[1].split(":");
                AxiusPlugin correct = null;
                for (AxiusPlugin plg : core.getRegisteredPlugins()) {
                    if(plg.getName().equalsIgnoreCase(parts[0])) correct = plg;
                }
                if(correct == null) {
                    sndr.sendMessage(getUsage());
                    return false;
                }

                int errorID = Integer.parseInt(parts[1]);
                if(errorID < correct.errors.size()) {
                    if(action.equals(ErrorActions.RESOLVE)) {
                        correct.errors.remove(errorID);
                        sndr.sendMessage(core.getNameFormatted() + "Successfully resolved error. (" + args[1] + ")");
                    }
                    else if (action.equals(ErrorActions.PRINT)) {
                        correct.errors.get(errorID).printStackTrace();
                        sndr.sendMessage(core.getNameFormatted() + "Successfully printed error stack to console. (" + args[1] + ")");
                    }
                    return true;
                }
            }
            sndr.sendMessage(getUsage());
        }
        else {
            CoreGUI cgui = new CoreGUI(core);
            cgui.show((Player)sndr);
        }

        return true;
    }
}
