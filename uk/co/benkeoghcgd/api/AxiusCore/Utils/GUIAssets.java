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
    public final static HashMap<Player, ItemStack> PLAYERHEADS = new HashMap<>();
    public final static HashMap<ItemStack, Player> PLAYERHEADS_INV = new HashMap<>();
    public final static HashMap<String, ItemStack> DECORHEADS = new HashMap<>();

    public static void generateDecor() {
        DECORHEADS.put("question", GenHead("MHF_Question"));
        DECORHEADS.put("exclamation", GenHead("MHF_Exclamation"));
        DECORHEADS.put("arrowright", GenHead("MHF_ArrowRight"));
        DECORHEADS.put("arrowleft", GenHead("MHF_ArrowLeft"));
        DECORHEADS.put("arrowup", GenHead("MHF_ArrowUp"));
        DECORHEADS.put("arrowdown", GenHead("MHF_ArrowDown"));

        DECORHEADS.put("fubbo", GenHead("Fubbo"));
    }

    public static void registerPlayer(Player p) {
        ItemStack head = GenHead(p);

        PLAYERHEADS.put(p, head);
        PLAYERHEADS_INV.put(head, p);
    }

    public static void unregisterPlayer(Player p) {
        PLAYERHEADS_INV.remove(PLAYERHEADS.get(p));
        PLAYERHEADS.remove(p);
    }

    public static int getInventorySize(int max) {
        if (max <= 0) return 9;
        int quotient = (int)Math.ceil(max / 9.0);
        return quotient > 5 ? 54: quotient * 9;
    }

    public static ItemStack GenHead(Player p) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta sm = (SkullMeta) head.getItemMeta();
        sm.setOwningPlayer(p);
        sm.setDisplayName(p.getName());
        head.setItemMeta(sm);

        return head;
    }

    @Deprecated(since = "1.1.0", forRemoval = true)
    public static ItemStack GenHead(String name) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta sm = (SkullMeta) head.getItemMeta();
        sm.setOwningPlayer(Bukkit.getOfflinePlayer(name));
        sm.setDisplayName(name);
        head.setItemMeta(sm);

        return head;
    }

    public static ItemStack GenHead(UUID uuid) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta sm = (SkullMeta) head.getItemMeta();
        OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);
        sm.setOwningPlayer(p);
        sm.setDisplayName(p.getName());
        head.setItemMeta(sm);

        return head;
    }
}