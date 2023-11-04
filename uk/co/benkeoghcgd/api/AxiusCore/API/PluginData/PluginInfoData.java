package uk.co.benkeoghcgd.api.AxiusCore.API.PluginData;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class PluginInfoData {

    /**
     * Holds icon data;
     */
    private HashMap<ItemStack, PluginInfoDataButton> buttons = new HashMap<>();

    /**
     * Adds a new icon to the buttons map
     * @param icon An itemstack which will act as a button in AxiusCore /core
     * @param method The Method you wish to be called upon clicking the button
     */
    public void addButton(ItemStack icon, PluginInfoDataButton method) {
        buttons.put(icon, method);
    }

    /**
     * Remove an existing icon from the buttons map
     * @param icon The itemstack of the icon you wish to remove
     */
    public void removeButton(ItemStack icon) {
        buttons.remove(icon);
    }

    /**
     * Remove an existing icon from the buttons map
     * @param icon The itemstack of the icon you wish to remove
     * @param method The Method of the icon you wish to remove
     */
    public void removeButton(ItemStack icon, PluginInfoDataButton method) {
        buttons.remove(icon, method);
    }

    /**
     * Edit the Method of an existing icon
     * @param icon The icon you wish to edit
     * @param newMethod The new Method, overriding the old Method
     */
    public void changeMethod(ItemStack icon, PluginInfoDataButton newMethod) {
        removeButton(icon);
        addButton(icon, newMethod);
    }

    /**
     * Access the button map
     * @return The button map
     */
    public HashMap<ItemStack, PluginInfoDataButton> getButtons() { return buttons; }
}
