package uk.co.benkeoghcgd.api.AxiusCore;

import org.bstats.bukkit.Metrics;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlayer;
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlugin;
import uk.co.benkeoghcgd.api.AxiusCore.API.Enums.PluginStatus;
import uk.co.benkeoghcgd.api.AxiusCore.API.Enums.UpdaterMethod;
import uk.co.benkeoghcgd.api.AxiusCore.API.PluginData.PluginInfoData;
import uk.co.benkeoghcgd.api.AxiusCore.API.PluginData.PublicPluginData;
import uk.co.benkeoghcgd.api.AxiusCore.API.Updating.*;
import uk.co.benkeoghcgd.api.AxiusCore.Commands.CoreCommand;
import uk.co.benkeoghcgd.api.AxiusCore.Commands.StreamerCommand;
import uk.co.benkeoghcgd.api.AxiusCore.DataHandlers.ConfigYML;
import uk.co.benkeoghcgd.api.AxiusCore.API.Exceptions.MissingDependException;
import uk.co.benkeoghcgd.api.AxiusCore.Listeners.CommandOverrideListener;
import uk.co.benkeoghcgd.api.AxiusCore.Listeners.JoinLeaveListener;
import uk.co.benkeoghcgd.api.AxiusCore.Utils.Logging;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static uk.co.benkeoghcgd.api.AxiusCore.API.Utilities.GUI.createGuiItem;

public class AxiusCore extends AxiusPlugin {

    // Instance Getter
    private static AxiusCore instance;
    private static boolean PushLocale = false;
    private static List<AxiusPlayer> player_pool = new ArrayList<>();

    public static void addPlayer(Player p) {
        player_pool.add(new AxiusPlayer(p));
    }

    public static void removePlayer(AxiusPlayer p) {
        player_pool.remove(p);
    }

    public static List<AxiusPlayer> getCurrentPlayers() {
        return player_pool;
    }


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
        CheckLocale();

        Logging.Log("Initializing Core.");
        PublicPluginData ppd = new PublicPluginData();
        PluginInfoData pid = new PluginInfoData();

        ppd.setPublicStatus(true);
        ppd.setUpdaterMethod(UpdaterMethod.SPIGOT);
        ppd.setSpigotID(102852);
        ppd.setVersionSeparator(".");

        SetPublicPluginData(ppd);


        Logging.Log("Readying metrics.");
        new Metrics(this, 15561);

        Logging.Log("Collecting commands.");
        commands.add(new CoreCommand(this));
        commands.add(new StreamerCommand(this));

        Logging.Log("Registering data files.");
        ConfigYML configYML = new ConfigYML(this);
        autoUpdate = (boolean) configYML.data.get("autoUpdatePlugins");
    }

    private void CheckLocale() {
        Locale locale = Locale.getDefault();
        if(!locale.getLanguage().equalsIgnoreCase("en")) {
                Logging.Log("Hey! I notice you're not from a natively English speaking country!");
                Logging.Log("Our plugins are actively looking translation into multiple other languages,");
                Logging.Log("and could really do with your help!! Please join the discord page located on");
                Logging.Log("our Spigot page! https://www.spigotmc.org/resources/axiuscore.102852/");
        }
    }

    @Override
    protected void Postregister() {
        setFormattedName("&x&f&b&3&6&3&6&lA&x&f&b&3&d&3&1&lX&x&f&c&4&4&2&d&lI&x&f&c&4&b&2&8&lU&x&f&c&5&2&2&3&lS&x&f&c&5&8&1&e&lC&x&f&d&5&f&1&a&lO&x&f&d&6&6&1&5&lR&x&f&d&6&d&1&0&lE&7 ");
        setIcon(createGuiItem(Material.COMMAND_BLOCK, "§c§lAxiusCore"));

        Logging.Log("Registering commands.");
        registerCommands();

        Logging.Log("Overriding default commands.");
        getServer().getPluginManager().registerEvents(new CommandOverrideListener(this), this);
        getServer().getPluginManager().registerEvents(new JoinLeaveListener(), this);
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
        if(_ppd != null && _ppd.getPublicStatus()) {
            Logging.Log("Public Resource: Plugin is public resource, adding Updater");

            Updater upd = null;

            if(_ppd.getUpdaterMethod() != UpdaterMethod.NO_UPDATE) {
                switch (_ppd.getUpdaterMethod()) {
                    case SPIGOT -> {
                        if(_ppd.getSpigotID() == Integer.MAX_VALUE) break;
                        if(_ppd.getVersionSeparator() != null) upd = new SpigotUpdaterMMP(plugin, _ppd.getSpigotID());
                        else upd = new SpigotUpdater(plugin, _ppd.getSpigotID());
                    }

                    case MAVEN -> {
                        if(_ppd.getMavenRepository() == null) break;
                        if(_ppd.getVersionSeparator() != null) upd = new MavenUpdaterMMP(plugin, _ppd.getMavenRepository());
                        else upd = new MavenUpdater(plugin, _ppd.getMavenRepository());
                    }
                }

                if(_ppd.getRegisterStatus() && upd != null) {
                    Logging.Log("Public Resource: Added Updater");
                }
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
     */
    public void unregisterPlugin(AxiusPlugin plugin) {
        registeredPlugins.remove(plugin);
    }
}