package uk.co.benkeoghcgd.api.AxiusCore.API;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.Arrays;

public abstract class AxiusCommand extends Command implements CommandExecutor {

    protected AxiusPlugin plugin;
    protected boolean canConsoleRun;

    /**
     * Constructor providing bulk of default command data;
     *
     * @param instance      Plugin Instance
     * @param canConsoleRun Can the console run this command?
     * @param name          Command itself, i.e. "core" for /core
     * @param description   Description of command
     * @param usage         Usage message
     * @param aliases       Command aliases, i.e. "c" and "co" for /core
     */
    public AxiusCommand(AxiusPlugin instance, boolean canConsoleRun, String name, String description, String usage, String... aliases) {
        super(name);

        this.canConsoleRun = canConsoleRun;
        plugin = instance;

        setAliases(Arrays.asList(aliases));
        setUsage(usage);
        setDescription(description);
    }

    @Override
    public boolean execute(CommandSender sndr, String s, String[] strings) {
        if(sndr instanceof ConsoleCommandSender && !canConsoleRun) {
            sndr.sendMessage(plugin.getNameFormatted() + "§cThis command can only be run as a player.");
            return false;
        }

        if(getPermission() != null && !getPermission().equalsIgnoreCase("null") && !sndr.hasPermission(getPermission())) {
            sndr.sendMessage(plugin.getNameFormatted() + "§c You don't have permission to run this command.");
            return false;
        }

        if(onCommand(sndr, this, s, strings))
            return true;

        sndr.sendMessage(plugin.getNameFormatted() + "§c You don't have permission to run this command.");
        return false;
    }
}
