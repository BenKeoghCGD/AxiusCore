package net.lenni0451.spm.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.Event;
import org.bukkit.plugin.*;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class PluginUtils {

    /**
     * Get the {@code plugins} directory
     *
     * @return The {@link File}
     */
    public File getPluginsDirectory() {
        return new File(".", "plugins");
    }

    /**
     * Get the update directory for plugins
     *
     * @return The {@link File}
     */
    public File getUpdateDirectory() {
        return new File(this.getPluginsDirectory(), "update");
    }

    /**
     * Get the {@link PluginManager} instance of the server
     *
     * @return The {@link PluginManager} instance
     */
    public PluginManager getPluginManager() {
        return Bukkit.getPluginManager();
    }

    /**
     * Enable a {@link Plugin} by its instance
     *
     * @param plugin The {@link Plugin} instance
     */
    public void enablePlugin(final Plugin plugin) {
        if (!plugin.isEnabled())
            this.getPluginManager().enablePlugin(plugin);
    }

    /**
     * Disable a {@link Plugin} by its instance
     *
     * @param plugin The {@link Plugin} instance
     */
    @SuppressWarnings("removal")
    public void disablePlugin(final Plugin plugin) {
        if (plugin.isEnabled()) {
            for (Thread thread : ThreadUtils.getAllThreadsFromClassLoader(plugin.getClass().getClassLoader())) {
                try {
                    thread.interrupt();
                    thread.join(2000);
                    if (thread.isAlive()) thread.stop();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
            this.getPluginManager().disablePlugin(plugin);
        }
    }

    /**
     * Load a {@link Plugin} by its instance<br>
     * See {@link #loadPlugin(String)} for more information
     *
     * @param plugin The {@link Plugin} instance
     * @throws IllegalStateException If anything goes wrong
     */
    public void loadPlugin(final Plugin plugin) {
        this.loadPlugin(plugin.getName());
    }

    /**
     * Load a {@link Plugin} by its name<br>
     * This tries to get the {@link Plugin} {@link File} by name
     * and if not found scans all {@link PluginDescriptionFile}s for the name
     *
     * @param name The name of the {@link Plugin}
     * @return The new {@link Plugin} instance
     * @throws IllegalStateException If anything goes wrong
     */
    public Plugin loadPlugin(final String name) {
        AtomicReference<File> targetFile = new AtomicReference<>(new File(this.getPluginsDirectory(), name + (name.toLowerCase().endsWith(".jar") ? "" : ".jar")));
        Plugin targetPlugin;

        if (!targetFile.get().exists()) {
            Arrays.stream(FileUtils.listFiles(this.getPluginsDirectory()))
                    .filter(file -> file.getName().toLowerCase().endsWith(".jar") || (!file.getName().toLowerCase().endsWith(".jar")))
                    .filter(file -> {
                        try {
                            PluginDescriptionFile desc = this.getPluginLoader().getPluginDescription(file);
                            return desc.getName().equalsIgnoreCase(name);
                        } catch (InvalidDescriptionException ignored) {
                            //Here we do not need to care about invalid plugin ymls
                        }
                        return false;
                    }).findAny().ifPresent(targetFile::set);
        }
        if (targetFile.get() == null) {
            throw new IllegalStateException("Plugin file not found");
        }
        this.updatePlugin(targetFile.get());

        try {
            targetPlugin = this.getPluginLoader().loadPlugin(targetFile.get());
        } catch (UnknownDependencyException e) {
            throw new IllegalStateException("Missing Dependency");
        } catch (InvalidPluginException e) {
            throw new IllegalStateException("Invalid plugin file");
        }

        targetPlugin.onLoad();
        this.enablePlugin(targetPlugin);
        try { //Get plugins list and add plugin if not already
            Field f = this.getPluginManager().getClass().getDeclaredField("plugins");
            f.setAccessible(true);
            List<Plugin> plugins = (List<Plugin>) f.get(this.getPluginManager());
            if (!plugins.contains(targetPlugin)) plugins.add(targetPlugin);
        } catch (Throwable e) {
            e.printStackTrace(); //We maybe even want to see why the plugin could not be added
//            throw new IllegalStateException("Unable to add to plugin list");
            throw new IllegalStateException();
        }
        return targetPlugin;
    }

    private PluginLoader getPluginLoader() {
        return uk.co.benkeoghcgd.api.AxiusCore.AxiusCore.getInstance().getPluginLoader();
    }

    /**
     * Unload a {@link Plugin} by its instance<br>
     * This closes the {@link URLClassLoader} of the {@link Plugin} which causes
     * the {@link Plugin} no longer being able to load new {@link Class}es
     *
     * @param plugin The {@link Plugin} instance
     * @throws IllegalStateException If anything goes wrong
     */
    public void unloadPlugin(final Plugin plugin) {
        this.disablePlugin(plugin);

        PluginManager pluginManager = this.getPluginManager();

        List<Plugin> plugins;
        Map<String, Plugin> lookupNames;
        SimpleCommandMap commandMap;
        Map<String, Command> knownCommands;
        Map<Event, SortedSet<RegisteredListener>> listeners;

        try { //Get plugins list
            Field f = pluginManager.getClass().getDeclaredField("plugins");
            f.setAccessible(true);
            plugins = (List<Plugin>) f.get(pluginManager);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new IllegalStateException("Unable to get plugins list");
        }
        try { //Get lookup names
            Field f = pluginManager.getClass().getDeclaredField("lookupNames");
            f.setAccessible(true);
            lookupNames = (Map<String, Plugin>) f.get(pluginManager);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new IllegalStateException("Unable to get lookup names");
        }
        try { //Get command map
            Field f = pluginManager.getClass().getDeclaredField("commandMap");
            f.setAccessible(true);
            commandMap = (SimpleCommandMap) f.get(pluginManager);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new IllegalStateException("Unable to get command map");
        }
        try { //Get known commands
            Field f = SimpleCommandMap.class.getDeclaredField("knownCommands");
            f.setAccessible(true);
            knownCommands = (Map<String, Command>) f.get(commandMap);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new IllegalStateException("Unable to get known commands");
        }
        try {
            Field f = pluginManager.getClass().getDeclaredField("listeners");
            f.setAccessible(true);
            listeners = (Map<Event, SortedSet<RegisteredListener>>) f.get(pluginManager);
        } catch (Throwable e) {
            listeners = null;
        }

        plugins.remove(plugin);
        lookupNames.remove(plugin.getName());
        { //Remove plugin commands
            Iterator<Map.Entry<String, Command>> iterator = knownCommands.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Command> entry = iterator.next();
                if (entry.getValue() instanceof PluginCommand) {
                    PluginCommand command = (PluginCommand) entry.getValue();
                    if (command.getPlugin().equals(plugin)) iterator.remove();
                }
            }
        }
        if (listeners != null) {
            for (Set<RegisteredListener> registeredListeners : listeners.values()) {
                registeredListeners.removeIf(registeredListener -> registeredListener.getPlugin().equals(plugin));
            }
        }

        if (plugin.getClass().getClassLoader() instanceof URLClassLoader) {
            URLClassLoader classLoader = (URLClassLoader) plugin.getClass().getClassLoader();

            try {
                classLoader.close();
            } catch (Throwable t) {
                throw new IllegalStateException("Unable to close the class loader");
            }
        }

        System.gc(); //Hopefully remove all leftover plugin classes and references
    }

    /**
     * Update a {@link Plugin} from the updates folder
     *
     * @param pluginFile The {@link File} of the {@link Plugin}
     * @return {@code true} if the update was successful
     */
    public boolean updatePlugin(final File pluginFile) {
        final File updateFile = new File(this.getUpdateDirectory(), pluginFile.getName());
        //TODO: Do some more advanced checking (Load plugin.yml and compare name, author, ...)
        if (!updateFile.exists()) return false;
        if (pluginFile.exists() && !pluginFile.delete()) return false;
        return updateFile.renameTo(pluginFile);
    }

}