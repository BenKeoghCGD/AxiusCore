package uk.co.benkeoghcgd.api.AxiusCore.API;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public abstract class GUI implements Listener {
    public Inventory container;
    public String title;
    public JavaPlugin plugin;

    /**
     * Should be utilized with container#setItem, or container#addItem to fill the GUI
     */
    protected abstract void Populate();

    /**
     * Embedded class within the existing event.
     * Extends functionality of onInventoryClick
     * @param e Standarad inventory click event
     */
    protected abstract void onInvClick(InventoryClickEvent e);

    /**
     * Constructor for GUI elements.
     * Providing these parameters creates an Inventory type called container which is the basis of the class
     * @param instance
     * @param rows
     * @param title
     * @see Inventory
     */
    public GUI(JavaPlugin instance, int rows, String title) {
        plugin = instance;
        this.title = ChatColor.translateAlternateColorCodes('&', title);
        this.container = Bukkit.createInventory(null, 9 * rows, this.title);

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Constructor for GUI elements.
     * Providing these parameters creates an Inventory type called container which is the basis of the class
     * @param instance
     * @param rows
     * @param title
     * @see Inventory
     */
    public GUI(AxiusPlugin instance, int rows, String title) {
        plugin = instance;
        this.title = ChatColor.translateAlternateColorCodes('&', title);
        this.container = Bukkit.createInventory(null, 9 * rows, this.title);

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Returns an ItemStack object that can then be added to a GUI
     * @param material base material for the ItemStack to base off
     * @param name     the final name of the ItemStack
     * @param lore     the final lore of the ItemStack, can take multiple String objects
     * @see   ItemStack
     * @return ItemStack type formatted for GUI use
     */
    public static ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Returns an ItemStack object that can then be added to a GUI
     * @param material base material for the ItemStack to base off
     * @param name     the final name of the ItemStack
     * @param lore     the final lore of the ItemStack, can take multiple String objects
     * @see   ItemStack
     * @return ItemStack type formatted for GUI use
     */
    public static ItemStack createGuiItem(final Material material, final String name, final List<String> lores) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lores);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Returns an ItemStack object that can then be added to a GUI
     * @param item     old ItemStack for the new ItemStack to base off
     * @param name     the final name of the ItemStack
     * @param lore     the final lore of the ItemStack, can take multiple String objects
     * @see   ItemStack
     * @return ItemStack type formatted for GUI use
     */
    public static ItemStack createGuiItem(final ItemStack item, final String name, final String... lore) {
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Returns an ItemStack object that can then be added to a GUI
     * @param item     old ItemStack for the new ItemStack to base off
     * @param name     the final name of the ItemStack
     * @param lore     the final lore of the ItemStack, can take multiple String objects
     * @see   ItemStack
     * @return ItemStack type formatted for GUI use
     */
    public static ItemStack createGuiItem(final ItemStack item, final String name, final List<String> lore) {
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent e) {
        if(!e.getInventory().equals(container)) return;
        e.setCancelled(true);

        onInvClick(e);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryDrag(final InventoryDragEvent e) {
        if(e.getInventory().equals(container)) e.setCancelled(true);
    }

    /**
     * Shows the GUI to the Player
     * @param p The player to show the GUI to
     */
    public void show(Player p) { p.openInventory(container); }
}
