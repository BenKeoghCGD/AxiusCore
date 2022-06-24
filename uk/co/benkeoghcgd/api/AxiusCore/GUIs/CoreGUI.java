package uk.co.benkeoghcgd.api.AxiusCore.GUIs;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import uk.co.benkeoghcgd.api.AxiusCore.API.Enums.PluginStatus;
import uk.co.benkeoghcgd.api.AxiusCore.API.GUI;
import uk.co.benkeoghcgd.api.AxiusCore.AxiusCore;
import uk.co.benkeoghcgd.api.AxiusCore.Utils.GUIAssets;

public class CoreGUI extends GUI {
    AxiusCore core;

    ItemStack authorItm;
    String msgStart = ChatColor.translateAlternateColorCodes('&', "&c&lAXIUSCORE&7 ");

    public CoreGUI(AxiusCore instance) {
        super(instance, 1, ChatColor.translateAlternateColorCodes('&', AxiusCore.PREFIX + "Home"));
        core = instance;
        Populate();
    }

    @Override
    protected void Populate() {
        String vers = ChatColor.translateAlternateColorCodes('&',
                "&7Plugin Version: &c" + plugin.getDescription().getVersion());
        String apivers = ChatColor.translateAlternateColorCodes('&',
                "&7API Version: &c" + plugin.getDescription().getAPIVersion());
        container.setItem(0, createGuiItem(Material.COMMAND_BLOCK, ChatColor.translateAlternateColorCodes('&', AxiusCore.PREFIX), vers, apivers));

        String author1 = ChatColor.translateAlternateColorCodes('&',
                "&7Core developed by &3Fubbo&7.");
        String author2 = ChatColor.translateAlternateColorCodes('&',
                "&cRight Click - View Portfolio");
        String author3 = ChatColor.translateAlternateColorCodes('&',
                "&aLeft Click - View Other Plugins");
        authorItm = createGuiItem(GUIAssets.DECORHEADS.get("fubbo"), ChatColor.translateAlternateColorCodes('&', "&3&lAUTHOR"), author1, "", author2, author3);
        container.setItem(8, authorItm);

        String loadedPlugins = ChatColor.translateAlternateColorCodes('&', "&7Plugins Hooked: &c" + core.registeredPlugins.size());
        int mal = (int) core.registeredPlugins.stream().filter(a -> a.pullStatus() == PluginStatus.MALFUNCTIONED).count();
        int op = (int) core.registeredPlugins.stream().filter(a -> a.pullStatus() == PluginStatus.OPERATIONAL).count();
        int run = (int) core.registeredPlugins.stream().filter(a -> a.pullStatus() == PluginStatus.RUNNING).count();
        String statuses = ChatColor.translateAlternateColorCodes('&', "&7Statuses: &a" + run + "&7:&6" + op + "&7:&c" + mal + "&7.");
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
