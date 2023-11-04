package uk.co.benkeoghcgd.api.AxiusCore.API;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import uk.co.benkeoghcgd.api.AxiusCore.AxiusCore;
import uk.co.benkeoghcgd.api.AxiusCore.DataHandlers.PlayerYML;

import java.util.UUID;

public class AxiusPlayer {
    private OfflinePlayer spigot_Player;
    private PlayerYML axiuscore_PlayerData;

    /**
     * Constructor for AxiusPlayer, setting defaults using Player type
     *
     * @param player reference to AxiusPlayer via Player type
     */
    public AxiusPlayer(Player player) {
        spigot_Player = player;
        init();
    }

    /**
     * Constructor for AxiusPlayer, Setting defaults using OfflinePlayer type
     *
     * @param player reference to AxiusPlayer via Player type
     */
    public AxiusPlayer(OfflinePlayer player) {
        spigot_Player = player;
        init();
    }

    /**
     * Constructor for AxiusPlayer, Setting defaults using OfflinePlayer type
     *
     * @param uuid reference to AxiusPlayer via UUID type
     */
    public AxiusPlayer(UUID uuid) {
        spigot_Player = Bukkit.getPlayer(uuid);
        init();
    }

    /**
     * Constructor for AxiusPlayer, Setting defaults using OfflinePlayer type
     *
     * @param UUIDorNAME reference to AxiusPlayer via String type - can be UUID as string, or player name as string
     */
    public AxiusPlayer(String UUIDorNAME) {
        spigot_Player = Bukkit.getPlayer(UUIDorNAME);
        init();
    }

    /**
     * Used to find the saved instance of an AxiusPlayer, using the player's Player type
     *
     * @param player the Player type used to find the AxiusPlayer
     * @return an AxiusPlayer derived from player
     */
    public static AxiusPlayer find(Player player) {
        for (AxiusPlayer p : AxiusCore.getCurrentPlayers()) {
            if(p.spigot_Player == player) return p;
        }
        return new AxiusPlayer(player);
    }

    /**
     * Initializes the AxiusPlayer type
     */
    private void init() {
        axiuscore_PlayerData = new PlayerYML(this);
    }

    /**
     * Get the name of the AxiusPlayer
     *
     * @return AxiusPlayer's username as String
     */
    public String getName() {
        return spigot_Player.getName();
    }

    /**
     * Get the UUID type of the AxiusPlayer
     *
      * @return AxiusPlayer's UUID
     */
    public UUID getUUID() {
        return spigot_Player.getUniqueId();
    }

    /**
     * Get the UUID string of the AxiusPlayer
     *
     * @return AxiusPlayer's UUID as String
     */
    public String getUUIDString() {
        return spigot_Player.getUniqueId().toString();
    }

    /**
     * Access the AxiusCore Streamer API, allows for developers to hide certain elements
     * from users who may be streaming, such as co-ordinates or IP addresses.
     *
     * @return whether user is streamer
     */
    public boolean isStreamer() {
        return (boolean)axiuscore_PlayerData.data.get("Streamer");
    }

    /**
     * Toggle streamer mode
     *
     * @return returns the new value of isStreamer()
     */
    public boolean toggleStreamer() {
        axiuscore_PlayerData.setData("Streamer", !((boolean)axiuscore_PlayerData.data.get("Streamer")), true);
        axiuscore_PlayerData.loadData();
        return (boolean)axiuscore_PlayerData.data.get("Streamer");
    }
}
