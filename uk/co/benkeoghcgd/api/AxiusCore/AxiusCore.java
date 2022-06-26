package uk.co.benkeoghcgd.api.AxiusCore;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlugin;
import uk.co.benkeoghcgd.api.AxiusCore.API.Enums.PluginStatus;
import uk.co.benkeoghcgd.api.AxiusCore.API.GUI;
import uk.co.benkeoghcgd.api.AxiusCore.Commands.CoreCommand;
import uk.co.benkeoghcgd.api.AxiusCore.Exceptions.MissingDependException;
import uk.co.benkeoghcgd.api.AxiusCore.Listeners.CommandOverrideListener;
import uk.co.benkeoghcgd.api.AxiusCore.Metrics.Metrics;
import uk.co.benkeoghcgd.api.AxiusCore.Utils.Logging;

import java.util.ArrayList;
import java.util.List;

public class AxiusCore extends AxiusPlugin {
    public static List<AxiusPlugin> registeredPlugins = new ArrayList<AxiusPlugin>();
    public static String PREFIX = ChatColor.translateAlternateColorCodes('&', "&x&f&b&3&6&3&6&lA&x&f&b&3&d&3&1&lX&x&f&c&4&4&2&d&lI&x&f&c&4&b&2&8&lU&x&f&c&5&2&2&3&lS&x&f&c&5&8&1&e&lC&x&f&d&5&f&1&a&lO&x&f&d&6&6&1&5&lR&x&f&d&6&d&1&0&lE&7 ");

    @Override
    protected void Preregister() {
        Logging.Log("Initializing Core.");
        Logging.Log("Readying metrics.");
        Metrics m = new Metrics(this, 15561);

        Logging.Log("Collecting commands.");
        commands.add(new CoreCommand(this));
    }

    @Override
    protected void Postregister() {
        setFormattedName(PREFIX);
        guiIcon = GUI.createGuiItem(Material.COMMAND_BLOCK, "§c§lAxiusCore");

        Logging.Log("Registering commands.");
        registerCommands();

        Logging.Log("Overriding default commands.");
        getServer().getPluginManager().registerEvents(new CommandOverrideListener(this), this);
    }

    @Override
    protected void Stop() {

    }

    @Override
    protected void FullStop() {

    }

    public boolean registerPlugin(AxiusPlugin plugin) {
        Logging.Log("Receiving hook request from: " + plugin.getName());
        if(registeredPlugins.contains(plugin)) {
            Logging.Log("Aborting request: Plugin already registered.");
            return false;
        }
        registeredPlugins.add(plugin);

        if(plugin.errors.size() > 0) {
            Logging.Warn("Faulty Hook: Plugin malfunctioned pre-hook.");
            for(Exception e : plugin.errors) {
                if(e instanceof MissingDependException) plugin.setStatus(PluginStatus.MALFUNCTIONED);
                Logging.Warn("           : " + e.getMessage());
            }
            getServer().getPluginManager().disablePlugin(plugin);
            return false;
        }

        Logging.Log("Successful Hook: Plugin successfully hooked.");
        return true;
    }

    public boolean unregisterPlugin(AxiusPlugin plugin) {
        if(registeredPlugins.contains(plugin)) {
            registeredPlugins.remove(plugin);
            return true;
        }
        return true;
    }
}