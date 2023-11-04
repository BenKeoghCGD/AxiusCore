package uk.co.benkeoghcgd.api.AxiusCore.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusCommand;
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlayer;
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlugin;

public class StreamerCommand extends AxiusCommand {

    public StreamerCommand(AxiusPlugin instance) {
        super(instance, false, "streamer", "Core command relating to streamer mode",
                instance.getNameFormatted() + " What went wrong?",
                "streaming", "str", "st");
        setPermission("null");
    }

    @Override
    public boolean onCommand(CommandSender sndr, Command command, String s, String[] args) {
        switch (args.length) {
            default:
                boolean b = AxiusPlayer.find((Player)sndr).toggleStreamer();
                sndr.sendMessage(plugin.getNameFormatted() + "§7 Toggled streamer mode to: " + (b ? "§a§lTRUE" : "§c§lFALSE") + "§7.");
                break;
        }
        return true;
    }
}
