package uk.co.benkeoghcgd.api.AxiusCore;

import org.bstats.bukkit.Metrics;
import org.bukkit.Material;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlugin;
import uk.co.benkeoghcgd.api.AxiusCore.API.Enums.PluginStatus;
import uk.co.benkeoghcgd.api.AxiusCore.API.Enums.VersionFormat;
import uk.co.benkeoghcgd.api.AxiusCore.API.Utilities.PublicPluginData;
import uk.co.benkeoghcgd.api.AxiusCore.API.Utilities.Updater;
import uk.co.benkeoghcgd.api.AxiusCore.Commands.CoreCommand;
import uk.co.benkeoghcgd.api.AxiusCore.DataHandlers.ConfigYML;
import uk.co.benkeoghcgd.api.AxiusCore.Exceptions.CoreSelfUpdateException;
import uk.co.benkeoghcgd.api.AxiusCore.Exceptions.MissingDependException;
import uk.co.benkeoghcgd.api.AxiusCore.Listeners.CommandOverrideListener;
import uk.co.benkeoghcgd.api.AxiusCore.Utils.Logging;

import java.util.ArrayList;
import java.util.List;

import static uk.co.benkeoghcgd.api.AxiusCore.API.GUI.createGuiItem;

public class AxiusCore extends AxiusPlugin {

    // Instance Getter
    private static AxiusCore instance;
    public static AxiusCore getInstance() {
        assert instance != null;
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        super.onEnable();
    }

    public boolean autoUpdate = false;

    
    // Registered Data
    private static final List<AxiusPlugin> registeredPlugins = new ArrayList<>();

    public List<AxiusPlugin> getRegisteredPlugins() { return registeredPlugins; }

    @Override
    protected void Preregister() {
        Logging.Log("Initializing Core.");
        EnableUpdater(102852, VersionFormat.MajorMinorPatch, "\\.");
        // Register new updater for AxiusCore

        Logging.Log("Readying metrics.");
        new Metrics(this, 15561);

        Logging.Log("Collecting commands.");
        commands.add(new CoreCommand(this));

        Logging.Log("Registering data files.");
        ConfigYML cyml = new ConfigYML(this);
        autoUpdate = (boolean) cyml.data.get("autoUpdatePlugins");
    }

    @Override
    protected void Postregister() {
        setFormattedName("&x&f&b&3&6&3&6&lA&x&f&b&3&d&3&1&lX&x&f&c&4&4&2&d&lI&x&f&c&4&b&2&8&lU&x&f&c&5&2&2&3&lS&x&f&c&5&8&1&e&lC&x&f&d&5&f&1&a&lO&x&f&d&6&6&1&5&lR&x&f&d&6&d&1&0&lE&7 ");
        setIcon(createGuiItem(Material.COMMAND_BLOCK, "§c§lAxiusCore"));

        Logging.Log("Registering commands.");
        registerCommands();

        Logging.Log("Overriding default commands.");
        getServer().getPluginManager().registerEvents(new CommandOverrideListener(this), this);
    }

    @Override
    protected void Stop() {}

    @Override
    protected void FullStop() {}

    /**
     * Registers a hook between your AxiusPlugin instance and AxiusCore.
     *
     * @param plugin Instance of an AxiusPlugin, which is to be registered.
     * @return Will return false in the event of registry failure. In most cases, this would be where you disable your plugin, as the updater has aborted the hook to update your plugin.
     */
    public boolean registerPlugin(AxiusPlugin plugin) {
        Logging.Log("Receiving hook request from: " + plugin.getName());

        Logging.Log("Checking Hook Data: Public Resource Checks");
        PublicPluginData _ppd = plugin.GetPublicPluginData();
        if(_ppd.getPublicStatus()) {
            Logging.Log("Public Resource: Plugin is public resource, adding Updater");

            try {
                if(!_ppd.getVersionFormat().equals(VersionFormat.NULL)) new Updater(plugin, _ppd.getSpigotResourceID(), _ppd.getVersionFormat(), _ppd.getVersionSeperator());
                else new Updater(plugin, _ppd.getSpigotResourceID());
            } catch (CoreSelfUpdateException e) {
                Logging.Err("Hook request from " + plugin.getName() + " failed: " + e.getMessage());
                return false;
            }

            if(_ppd.getRegisterStatus()) {
                Logging.Log("Public Resource: Added Updater");
            }
        }

        if(!_ppd.getRegisterStatus()) {
            Logging.Log("Aborting request: Plugin updating.");
            return false;
        }

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

    /**
     * Removes your AxiusPlugin's hook from AxiusCore
     * @param plugin Plugin instance which is to be removed
     * @return returns false in the event the plugin registry contains no instance of your AxiusPlugin
     */
    public boolean unregisterPlugin(AxiusPlugin plugin) {
        if(registeredPlugins.contains(plugin)) {
            registeredPlugins.remove(plugin);
            return true;
        }
        return false;
    }
}