package uk.co.benkeoghcgd.api.AxiusCore.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashMap;
import java.util.UUID;

public final class GUIAssets {
    static HashMap<Player, ItemStack> playerHeadCache = new HashMap<>();
    static HashMap<String, ItemStack> decorativeHeadCache = new HashMap<>();

    /**
     * Get a key-value-pair set of player heads cached on player login.
     *
     * @return a key-value-pair set of player heads cached on player login
     */
    public static HashMap<Player, ItemStack> getPlayerHeadCache() { return playerHeadCache; }

    /**
     * Get a key-value-pair set of decorative player heads cached on server launch.
     *
     * @return a key-value-pair set of decorative player heads cached on server launch
     */
    public static HashMap<String, ItemStack> getDecorHeadCache() { return decorativeHeadCache; }

    /**
     * Generate default decorative heads;
     * question
     * exclamation
     * arrowright
     * arrowleft
     * arrowup
     * arrowdown
     */
    public static void generateDecor() {
        decorativeHeadCache.put("question", GenHead("MHF_Question"));
        decorativeHeadCache.put("exclamation", GenHead("MHF_Exclamation"));
        decorativeHeadCache.put("arrowright", GenHead("MHF_ArrowRight"));
        decorativeHeadCache.put("arrowleft", GenHead("MHF_ArrowLeft"));
        decorativeHeadCache.put("arrowup", GenHead("MHF_ArrowUp"));
        decorativeHeadCache.put("arrowdown", GenHead("MHF_ArrowDown"));

        decorativeHeadCache.put("fubbo", GenHead("Fubbo"));
    }

    /**
     * Register a player to the GUIAssets class; This adds the player's head to the
     * playerhead cache, allowing you to grab heads without skin load times
     *
     * @param p Player who is being registered to the cache
     */
    public static void registerPlayer(Player p) {
        ItemStack head = GenHead(p);

        playerHeadCache.put(p, head);
    }

    /**
     * Unregister a player to the GUIAssets class; This removes all references to the
     * player from caches.
     *
     * @param p Player who is being removed from the cache
     */
    public static void unregisterPlayer(Player p) {
        playerHeadCache.remove(p);
    }

    /**
     * Rounds the given number to a multiple of 9.
     * Super useful for calculating inventory sizes for non-static data sets.
     *
     * @param items Number of slots to round.
     * @return a number from 9-54 (1-6 rows in inventory)
     */
    public static int getInventorySlots(int items) {
        if (items <= 0) return 9;
        int quotient = (int) Math.ceil(items / 9.0);
        return quotient > 5 ? 54: quotient * 9;
    }

    /**
     * Rounds the given number to a number of rows in an inventory.
     * i.e. 1 item rounds to 1 row, as does 9 items.
     *
     * @param items Number
     * @return a number within the minimum and maximum size range of an inventory.
     */
    public static int getInventoryRows(int items) {
        return getInventorySlots(items) / 9;
    }

    /**
     * Generate an ItemStack of a players skull
     *
     * @param p The player whos skull is being generated
     * @return an ItemStack type containing the player's skull
     */
    public static ItemStack GenHead(Player p) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta sm = (SkullMeta) head.getItemMeta();

        assert sm != null;
        sm.setOwningPlayer(p);
        sm.setDisplayName(p.getName());
        head.setItemMeta(sm);

        return head;
    }

    /**
     * Generate an ItemStack of a players skull
     *
     * @param name The player whos skull is being generated
     * @return an ItemStack type containing the player's skull
     */
    @Deprecated(since = "1.1.0", forRemoval = true)
    public static ItemStack GenHead(String name) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta sm = (SkullMeta) head.getItemMeta();

        assert sm != null;
        sm.setOwningPlayer(Bukkit.getOfflinePlayer(name));
        sm.setDisplayName(name);
        head.setItemMeta(sm);

        return head;
    }

    /**
     * Generate an ItemStack of a players skull
     *
     * @param uuid The player whos skull is being generated
     * @return an ItemStack type containing the player's skull
     */
    public static ItemStack GenHead(UUID uuid) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta sm = (SkullMeta) head.getItemMeta();
        OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);

        assert sm != null;
        sm.setOwningPlayer(p);
        sm.setDisplayName(p.getName());
        head.setItemMeta(sm);

        return head;
    }
}