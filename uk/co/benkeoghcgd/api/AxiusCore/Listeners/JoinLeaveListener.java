package uk.co.benkeoghcgd.api.AxiusCore.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlayer;
import uk.co.benkeoghcgd.api.AxiusCore.AxiusCore;

public class JoinLeaveListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        AxiusCore.addPlayer(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLeave(PlayerQuitEvent e) {
        AxiusPlayer p = AxiusPlayer.find(e.getPlayer());
        if(p != null) AxiusCore.removePlayer(p);
    }

}
