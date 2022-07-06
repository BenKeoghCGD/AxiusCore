package uk.co.benkeoghcgd.api.AxiusCore.GUIs;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlugin;
import uk.co.benkeoghcgd.api.AxiusCore.API.Enums.PluginStatus;
import uk.co.benkeoghcgd.api.AxiusCore.API.GUI;
import uk.co.benkeoghcgd.api.AxiusCore.AxiusCore;
import uk.co.benkeoghcgd.api.AxiusCore.Utils.GUIAssets;

import java.util.List;

public class CoreGUI extends GUI {
    AxiusCore core;
    String msgStart = ChatColor.translateAlternateColorCodes('&', "&c&lAXIUSCORE&7 ");

    public CoreGUI(AxiusCore instance) {
        super(instance, 1, ChatColor.translateAlternateColorCodes('&', instance.getNameFormatted() + "Home"));
        core = instance;
        Populate();
    }

    @Override
    protected void Populate() {
        // refresh all plugin statuses
        for(AxiusPlugin plug : core.getRegisteredPlugins()) plug.refreshStatus();

        // format all titles and lores.
        String vers = ChatColor.translateAlternateColorCodes('&', "&7Plugin Version: &c" + plugin.getDescription().getVersion());
        String apivers = ChatColor.translateAlternateColorCodes('&', "&7API Version: &c" + plugin.getDescription().getAPIVersion());
        String author1 = ChatColor.translateAlternateColorCodes('&', "&7Core developed by &3Fubbo&7.");
        String author2 = ChatColor.translateAlternateColorCodes('&', "&cRight Click - View Portfolio");
        String author3 = ChatColor.translateAlternateColorCodes('&', "&aLeft Click - View Other Plugins");

        List<AxiusPlugin> registeredPlugins = core.getRegisteredPlugins();
        String loadedPlugins = ChatColor.translateAlternateColorCodes('&', "&7Plugins Hooked: &c" + registeredPlugins.size());
        int mal = (int) registeredPlugins.stream().filter(a -> a.pullStatus() == PluginStatus.MALFUNCTIONED).count();
        int op = (int) registeredPlugins.stream().filter(a -> a.pullStatus() == PluginStatus.OPERATIONAL).count();
        int run = (int) registeredPlugins.stream().filter(a -> a.pullStatus() == PluginStatus.RUNNING).count();
        String statuses = ChatColor.translateAlternateColorCodes('&', "&7Statuses: &a" + run + "&7:&6" + op + "&7:&c" + mal + "&7.");

        // Add generated items to GUI
        container.setItem(8, createGuiItem(GUIAssets.getDecorHeadCache().get("fubbo"), ChatColor.translateAlternateColorCodes('&', "&3&lAUTHOR"), author1, "", author2, author3));
        container.setItem(0, createGuiItem(Material.COMMAND_BLOCK, ChatColor.translateAlternateColorCodes('&', core.getNameFormatted()), vers, apivers));
        container.setItem(4, createGuiItem(Material.PAPER, ChatColor.translateAlternateColorCodes('&', "&c&lPLUGINS"), loadedPlugins, statuses));
    }

    @Override
    protected void onInvClick(InventoryClickEvent e) {
        if(e.getSlot() == 8) {
            TextComponent start = new TextComponent(msgStart);
            TextComponent cHere = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&cClick Here&7"));
            if(e.getClick() == ClickType.LEFT) {
                TextComponent end = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&7 to view my portfolio website."));
                cHere.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.benkeoghcgd.co.uk/"));

                start.addExtra(cHere);
                start.addExtra(end);
                e.getWhoClicked().spigot().sendMessage(start);
            }
            else if (e.getClick() == ClickType.RIGHT) {
                TextComponent end = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&7 to view my other plugins."));
                cHere.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/authors/benjamin223515.153279/"));

                start.addExtra(cHere);
                start.addExtra(end);
                e.getWhoClicked().spigot().sendMessage(start);
            }
            e.getWhoClicked().closeInventory();
        }

        if(e.getSlot() == 4) {
            e.getWhoClicked().closeInventory();
            PluginsGUI pgui = new PluginsGUI(core);
            pgui.show((Player)e.getWhoClicked());
        }
    }
}
