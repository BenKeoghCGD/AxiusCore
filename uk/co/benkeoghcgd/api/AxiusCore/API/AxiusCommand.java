package uk.co.benkeoghcgd.api.AxiusCore.API;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public abstract class AxiusCommand extends Command implements CommandExecutor {

    protected AxiusPlugin plugin;
    protected boolean canConsoleRun;

    public AxiusCommand(AxiusPlugin instance, boolean canConsoleRun, String name, String description, String usage, String... aliases) {
        super(name);

        this.canConsoleRun = canConsoleRun;
        plugin = instance;

        setAliases(Arrays.asList(aliases));
        setUsage(usage);
        setDescription(description);
    }

    @Deprecated
    public AxiusCommand(JavaPlugin instance, boolean canConsoleRun, String name, String description, String usage, String... aliases) {
        super(name);

        this.canConsoleRun = canConsoleRun;
        plugin = (AxiusPlugin) instance;

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

        if(getPermission() != null && !sndr.hasPermission(getPermission())) {
            sndr.sendMessage(plugin.getNameFormatted() + "§c You don't have permission to run this command.");
            return false;
        }

        if(onCommand(sndr, this, s, strings))
            return true;

        sndr.sendMessage(plugin.getNameFormatted() + "§c You don't have permission to run this command.");
        return false;
    }
}
