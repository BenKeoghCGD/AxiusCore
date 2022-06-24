package uk.co.benkeoghcgd.api.AxiusCore.GUIs;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlugin;
import uk.co.benkeoghcgd.api.AxiusCore.API.GUI;
import uk.co.benkeoghcgd.api.AxiusCore.AxiusCore;
import uk.co.benkeoghcgd.api.AxiusCore.Utils.GUIAssets;

import java.util.HashMap;

class PluginsGUI extends GUI {

    HashMap<Integer, AxiusPlugin> selection = new HashMap<>();

    public PluginsGUI(AxiusCore instance) {
        super(instance, GUIAssets.getInventorySize(AxiusCore.registeredPlugins.size()) / 9, ChatColor.translateAlternateColorCodes('&', AxiusCore.PREFIX + "Home"));
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
            ItemStack itm = createGuiItem(plug.getIcon(), plug.getNameFormatted(), "§7Status: " + status, "", "§aLeft-Click to view info.", "§cRight-Click to view error list");
            container.addItem(itm);
            selection.put(index, plug);
            index++;
        }
    }

    @Override
    protected void onInvClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if(e.getSlot() <= (selection.size() - 1)) {
            if(e.getClick() == ClickType.RIGHT) {
                p.sendMessage();
                return;
            }

            if(e.getClick() == ClickType.LEFT) {
                // Plugin info
                return;
            }
        }
    }
}
