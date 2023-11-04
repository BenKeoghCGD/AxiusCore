package uk.co.benkeoghcgd.api.AxiusCore.GUIs;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlugin;
import uk.co.benkeoghcgd.api.AxiusCore.API.PluginData.PluginInfoDataButton;
import uk.co.benkeoghcgd.api.AxiusCore.API.Utilities.GUI;
import uk.co.benkeoghcgd.api.AxiusCore.AxiusCore;
import uk.co.benkeoghcgd.api.AxiusCore.Utils.GUIAssets;
import uk.co.benkeoghcgd.api.AxiusCore.Utils.Logging;

import java.util.HashMap;

public class PluginInfoGUI extends GUI {

    AxiusPlugin target;
    HashMap<ItemStack, PluginInfoDataButton> buttons = new HashMap<>();

    public PluginInfoGUI(AxiusCore instance, AxiusPlugin plugin) {
        super(instance, GUIAssets.getInventoryRows(plugin.GetPluginInfoData().getButtons().size()), instance.getNameFormatted() + "§7 // " + plugin.getNameFormatted());

        target = plugin;
        Populate();
    }

    @Override
    protected void Populate() {
        buttons = target.GetPluginInfoData().getButtons();

        for(ItemStack is : buttons.keySet()) {
            container.addItem(is);
        }
    }

    @Override
    protected void onInvClick(InventoryClickEvent e) {
        if(e.getCurrentItem() == null) return;
        if(e.getCurrentItem().getType() == Material.AIR) return;
        if(!buttons.containsKey(e.getCurrentItem())) return;

        buttons.get(e.getCurrentItem()).execute(e.getWhoClicked());
    }
}
