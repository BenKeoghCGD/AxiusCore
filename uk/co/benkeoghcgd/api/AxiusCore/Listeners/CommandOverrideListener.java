package uk.co.benkeoghcgd.api.AxiusCore.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlugin;
import uk.co.benkeoghcgd.api.AxiusCore.AxiusCore;

public class CommandOverrideListener implements Listener {

    AxiusCore plugin;

    public CommandOverrideListener(AxiusCore axiusCore) {
        plugin = axiusCore;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPreCommand(PlayerCommandPreprocessEvent e) {
        String[] args = e.getMessage().split(" ");
        String command = args[0];

        switch (command.toLowerCase()) {
            case "/pl", "/plugins" -> {
                e.setCancelled(true);
                if (e.getPlayer().hasPermission("AxiusCore.Plugins")) showPlugins(e.getPlayer(), args.length == 1);
                else
                    e.getPlayer().sendMessage(plugin.getNameFormatted() + "You dont have permission to view the plugin list.");
            }
        }
    }

    // Override for /plugins
    public void showPlugins(Player sndr, boolean all) {
        sndr.sendMessage(plugin.getNameFormatted() + "Plugin List (" + (all ? "ALL" : "REGISTERED") + ")");
        StringBuilder registered = new StringBuilder();
        StringBuilder all2 = new StringBuilder();

        boolean first = true;
        for(Plugin plug : plugin.getServer().getPluginManager().getPlugins()) {
            if(plug instanceof AxiusPlugin) {
                if(registered.toString().equals("")) first = true;
                if(plugin.getRegisteredPlugins().contains((AxiusPlugin) plug)) registered.append(first ? "" : "§7, ").append("§a§l").append(plug.getName());
                else registered.append(first ? "" : "§7, ").append("§c§l").append(plug.getName());
            }
            else {
                if(all2.toString().equals("")) first = true;
                all2.append(first ? "" : "§7, ").append("§a").append(plug.getName());
            }
            first = false;
        }

        sndr.sendMessage("§8Registered With Core: " + registered);
        if(all) sndr.sendMessage("§8Not Registered: " + all2);
    }
}
