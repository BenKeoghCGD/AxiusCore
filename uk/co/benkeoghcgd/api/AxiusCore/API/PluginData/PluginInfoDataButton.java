package uk.co.benkeoghcgd.api.AxiusCore.API.PluginData;

import org.bukkit.command.CommandSender;

public abstract class PluginInfoDataButton {
    public abstract void execute();

    public void execute(CommandSender sender) {
        execute();
    }
}
