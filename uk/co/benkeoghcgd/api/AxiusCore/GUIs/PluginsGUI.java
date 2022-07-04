package uk.co.benkeoghcgd.api.AxiusCore.GUIs;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlugin;
import uk.co.benkeoghcgd.api.AxiusCore.API.GUI;
import uk.co.benkeoghcgd.api.AxiusCore.API.Utilities.PublicPluginData;
import uk.co.benkeoghcgd.api.AxiusCore.AxiusCore;
import uk.co.benkeoghcgd.api.AxiusCore.Utils.GUIAssets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class PluginsGUI extends GUI {

    HashMap<Integer, AxiusPlugin> selection = new HashMap<>();
    AxiusCore instance;

    public PluginsGUI(AxiusCore instance) {
        super(instance, GUIAssets.getInventorySize(AxiusCore.registeredPlugins.size()) / 9, ChatColor.translateAlternateColorCodes('&', AxiusCore.PREFIX + "Home"));
        this.instance = instance;
        Populate();
    }

    @Override
    protected void Populate() {
        int index = 0;
        for(AxiusPlugin plug : AxiusCore.registeredPlugins) {
            String status = "";
            switch(plug.pullStatus()) {
                case OPERATIONAL:
                    status = "§6§lOPERATIONAL";
                    break;
                case RUNNING:
                    status = "§a§lRUNNING";
                    break;
                case MALFUNCTIONED:
                    status = "§c§lMALFUNCTIONED";
                    break;
            }

            PublicPluginData ppd = plug.GetPublicPluginData();

            List<String> lores = new ArrayList<>();
            lores.add("§7Status: " + status);
            if(ppd.isPublicResource) {
                lores.add("");
                lores.add("§a§lPUBLIC PLUGIN");
                lores.add("§7Plugin ID: §a" + ppd.SpigotResourceID);
            }
            lores.add("");
            lores.add("§aLeft-Click to view info.");
            lores.add("§cRight-Click to view error list");

            if(ppd.isPublicResource) lores.add("§a§lSHIFT + Left-Click to view Spigot page");

            ItemStack itm = createGuiItem(plug.getIcon(), plug.getNameFormatted(), lores);
            container.addItem(itm);
            selection.put(index, plug);
            index++;
        }
    }

    @Override
    protected void onInvClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if(e.getSlot() <= (selection.size() - 1)) {
            AxiusPlugin plug = selection.get(e.getSlot());
            if(e.getClick() == ClickType.RIGHT) {
                if(plug.errors.isEmpty()) {
                    p.sendMessage(instance.PREFIX + "This plugin is running normally.");
                }
                else {
                    p.sendMessage(instance.PREFIX + "§8Error list for plugin: " + plug.getNameFormatted() + "§8 (Length: " + plug.errors.size() + ")");
                    int index = 1;
                    for(Exception ex : plug.errors) {
                        p.sendMessage("§8[" + index + "]§7 " + ex.getClass().getSimpleName());
                        TextComponent First = new TextComponent("§8[" + index + "] ");
                        TextComponent printToConsole = new TextComponent("§e[PRINT TO CONSOLE] ");
                        TextComponent ResolveError = new TextComponent("§a[RESOLVE] ");

                        printToConsole.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eClick to print error stack to console.").create()));
                        printToConsole.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/core print " + plug.getName() + ":" + (index-1)));
                        ResolveError.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aClick to resolve error.").create()));
                        ResolveError.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/core resolve " + plug.getName() + ":" + (index-1)));

                        First.addExtra(printToConsole);
                        First.addExtra(ResolveError);

                        p.spigot().sendMessage(First);
                        index++;
                    }
                }
                return;
            }

            if(e.getClick() == ClickType.LEFT) {
                // Plugin info
                return;
            }

            if(e.getClick() == ClickType.SHIFT_LEFT) {

                if(plug.GetPublicPluginData().isPublicResource) {
                    TextComponent First = new TextComponent("§c§lAxiusCore§7 Click ");
                    TextComponent btn = new TextComponent("§cHERE");

                    btn.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7 This is a direct link to §6spigotmc.org§7.").create()));
                    btn.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/" + plug.GetPublicPluginData().SpigotResourceID + "/"));

                    TextComponent last = new TextComponent("§7to view the Spigot page for " + plug.getName() + ".");

                    First.addExtra(btn);
                    First.addExtra(last);

                    p.spigot().sendMessage(First);
                }
                else {
                    p.sendMessage(plug.getNameFormatted() + "You can not shift-click this.");
                }

            }

            if(e.getClick() == ClickType.SHIFT_RIGHT) {

            }
        }
    }
}
