package uk.co.benkeoghcgd.api.AxiusCore.API;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.benkeoghcgd.api.AxiusCore.API.Utilities.PublicPluginData;
import uk.co.benkeoghcgd.api.AxiusCore.AxiusCore;
import uk.co.benkeoghcgd.api.AxiusCore.API.Enums.PluginStatus;
import uk.co.benkeoghcgd.api.AxiusCore.Exceptions.CommandRegisterException;
import uk.co.benkeoghcgd.api.AxiusCore.Utils.GUIAssets;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class AxiusPlugin extends JavaPlugin {

    JavaPlugin instance;
    public AxiusPlugin() {
        instance = this;
    }


    public List<Command> commands = new ArrayList<>();
    public List<Exception> errors = new ArrayList<>();
    protected PluginStatus status = PluginStatus.RUNNING;

    public AxiusCore core;
    public long lastUpdate = 0L;
    private static CommandMap commandMap;

    // Plugin Data
    String nameFormatted;
    ItemStack guiIcon;
    PublicPluginData ppd = new PublicPluginData();

    static {
        try {
            Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            f.setAccessible(true);
            commandMap = (CommandMap) f.get(Bukkit.getServer());
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    protected void EnableUpdater(int SpigotResourceID) {
        ppd.currentPluginVersion = this.getDescription().getVersion();
        ppd.SpigotResourceID = SpigotResourceID;
        ppd.isPublicResource = true;
    }

    public PublicPluginData GetPublicPluginData() {
        return ppd;
    }
    public void SetPublicPluginData(PublicPluginData ppd) { this.ppd = ppd; }

    public ItemStack getIcon() {
        return guiIcon;
    }
    public void setIcon(ItemStack stack) { guiIcon = stack; }

    public String getNameFormatted() {
        return nameFormatted;
    }

    protected void setFormattedName(String name) {
        nameFormatted = ChatColor.translateAlternateColorCodes('&', name);
    }

    /**
     * Intermittent function that runs before the plugin self-registers to the core.
     * Should be overriden to implement pre-registry tasks like command generation
     * and listener registration.
     */
    protected abstract void Preregister();

    /**
     * Intermittent function that runs after the plugin self-registers to the core.
     * Should be overriden to implement post-registry tasks, such as those which depend
     * on the core.
     */
    protected abstract void Postregister();

    /**
     * Intermittent function that runs before the plugin deregisters itself from the core.
     * Should be overriden to implement pre-shut down procedures.
     */
    protected abstract void Stop();

    /**
     * Intermittent function that runs after the plugin deregisters itself from the core.
     * Should be overriden to implement post-register shut down procedures.
     */
    protected abstract void FullStop();

    @Override
    public void onEnable() {
        core = (AxiusCore) getServer().getPluginManager().getPlugin("AxiusCore");
        Preregister();
        GUIAssets.generateDecor();
        lastUpdate = System.currentTimeMillis();
        if(!core.registerPlugin(this)) return;
        Postregister();
    }

    @Deprecated
    private void Register() {
        core.registerPlugin(this);
    }

    @Override
    public void onDisable() {
        Stop();
        Unregister();
        FullStop();
    }

    private void Unregister() {
        core.unregisterPlugin(this);
    }

    protected void registerCommands() {
        for(Command c : commands) {
            if(!commandMap.register(getName(), c))
                errors.add(new CommandRegisterException(c));
        }
    }

    public PluginStatus pullStatus() {
        return status;
    }
    public void setStatus(PluginStatus newStatus) {status = newStatus;}

    public void refreshStatus() {
        if(errors.size() == 0) status = PluginStatus.RUNNING;
        if(errors.size() > 0) status = PluginStatus.OPERATIONAL;
        if(errors.size() > 5) status = PluginStatus.MALFUNCTIONED;
    }


    public File pluginFile() {
        return this.getFile();
    }
}
