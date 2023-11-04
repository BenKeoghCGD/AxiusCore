package uk.co.benkeoghcgd.api.AxiusCore.GUIs;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlugin;
import uk.co.benkeoghcgd.api.AxiusCore.API.Utilities.GUI;
import uk.co.benkeoghcgd.api.AxiusCore.API.PluginData.PublicPluginData;
import uk.co.benkeoghcgd.api.AxiusCore.AxiusCore;
import uk.co.benkeoghcgd.api.AxiusCore.Utils.GUIAssets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class PluginsGUI extends GUI {

    HashMap<Integer, AxiusPlugin> selection = new HashMap<>();
    AxiusCore instance;

    public PluginsGUI(AxiusCore instance) {
        super(instance, GUIAssets.getInventoryRows(instance.getRegisteredPlugins().size()), ChatColor.translateAlternateColorCodes('&', instance.getNameFormatted() + "Home"));
        this.instance = instance;
        Populate();
    }

    @Override
    protected void Populate() {
        int index = 0;
        for(AxiusPlugin plug : instance.getRegisteredPlugins()) {
            String status = switch (plug.pullStatus()) {
                case OPERATIONAL -> "§6§lOPERATIONAL";
                case RUNNING -> "§a§lRUNNING";
                case MALFUNCTIONED -> "§c§lMALFUNCTIONED";
            };

            PublicPluginData ppd = plug.GetPublicPluginData();

            List<String> lores = new ArrayList<>();
            lores.add("§7Status: " + status);
            if(ppd.getPublicStatus()) {
                lores.add("");
                lores.add("§a§lPUBLIC PLUGIN");
                lores.add("§7Plugin ID: §a" + ppd.getSpigotID());
            }
            lores.add("");
            if(plug.GetPluginInfoData() != null && !plug.GetPluginInfoData().getButtons().isEmpty()) lores.add("§aLeft-Click to view info.");
            lores.add("§cRight-Click to view error list");

            if(ppd.getPublicStatus()) lores.add("§a§lSHIFT + Left-Click to view Spigot page");

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
                    p.sendMessage(instance.getNameFormatted() + "This plugin is running normally.");
                }
                else {
                    p.sendMessage(instance.getNameFormatted() + "§8Error list for plugin: " + plug.getNameFormatted() + "§8 (Length: " + plug.errors.size() + ")");
                    int index = 1;
                    for(Exception ex : plug.errors) {
                        p.sendMessage("§8[" + index + "]§7 " + ex.getClass().getSimpleName());
                        TextComponent First = new TextComponent("§8[" + index + "] ");
                        TextComponent printToConsole = new TextComponent("§e[PRINT TO CONSOLE] ");
                        TextComponent ResolveError = new TextComponent("§a[RESOLVE] ");

                        printToConsole.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§eClick to print error stack to console.")));
                        printToConsole.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/core print " + plug.getName() + ":" + (index-1)));
                        ResolveError.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§aClick to resolve error.")));
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
                if(plug.GetPluginInfoData() == null) return;
                e.getWhoClicked().closeInventory();
                PluginInfoGUI pig = new PluginInfoGUI(instance, plug);
                pig.show(((Player) e.getWhoClicked()));
                return;
            }

            if(e.getClick() == ClickType.SHIFT_RIGHT) {
                // Unknown as of now
                return;
            }

            if(e.getClick() == ClickType.SHIFT_LEFT) {
                if(plug.GetPublicPluginData().getPublicStatus()) {
                    TextComponent First = new TextComponent("§c§lAxiusCore§7 Click ");
                    TextComponent btn = new TextComponent("§cHERE");

                    btn.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§7 This is a direct link to §6spigotmc.org§7.")));
                    btn.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/" + plug.GetPublicPluginData().getSpigotID() + "/"));

                    TextComponent last = new TextComponent("§7to view the Spigot page for " + plug.getName() + ".");

                    First.addExtra(btn);
                    First.addExtra(last);

                    p.spigot().sendMessage(First);
                }
                else {
                    p.sendMessage(plug.getNameFormatted() + "You can not shift-click this.");
                }
            }
        }
    }
}
