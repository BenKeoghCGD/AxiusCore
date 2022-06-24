package uk.co.benkeoghcgd.api.AxiusCore.Listeners;

import org.bukkit.command.CommandSender;
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

        switch(command.toLowerCase()) {
            case "/pl":
            case "/plugins":
                e.setCancelled(true);

                // very rudimentary, will be updated at some point
                boolean all;
                if (args.length == 1) all = true;
                else all = false;

                if(e.getPlayer().hasPermission("AxiusCore.Plugins")) showPlugins(e.getPlayer(), all);
                else e.getPlayer().sendMessage(plugin.PREFIX + "You dont have permission to view the plugin list.");
                break;

        }
    }

    // Override for /plugins
    //              /pl

    public void showPlugins(Player sndr, boolean all) {
        sndr.sendMessage(plugin.PREFIX + "Plugin List (" + (all ? "ALL" : "REGISTERED") + ")");
        String registered = "";
        String all2 = "";

        boolean first = true;
        for(Plugin plug : plugin.getServer().getPluginManager().getPlugins()) {
            if(plug instanceof AxiusPlugin) {
                if(registered == "") first = true;
                if(AxiusCore.registeredPlugins.contains((AxiusPlugin) plug)) registered += (first ? "" : "§7, ") + "§a§l" + plug.getName();
                else registered += (first ? "" : "§7, ") + "§c§l" + plug.getName();
            }
            else {
                if(all2 == "") first = true;
                all2 += (first ? "" : "§7, ") + "§a" + plug.getName();
            }
            first = false;
        }

        sndr.sendMessage("§8Registered With Core: " + registered);
        if(all) sndr.sendMessage("§8Not Registered: " + all2);
    }
}
