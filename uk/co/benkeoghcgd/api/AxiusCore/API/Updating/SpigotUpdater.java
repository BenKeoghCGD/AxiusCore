package uk.co.benkeoghcgd.api.AxiusCore.API.Updating;

import org.json.JSONObject;
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlugin;
import uk.co.benkeoghcgd.api.AxiusCore.Utils.Logging;

import javax.annotation.Nullable;
import java.net.MalformedURLException;
import java.net.URL;

public class SpigotUpdater extends Updater {
    private int PluginID;

    public SpigotUpdater(AxiusPlugin plugin, int PluginID) {
        super(plugin);
        this.PluginID = PluginID;

        switch (compare()) {
            case -1:
                Logging.Log("Hooked plugin: " + plugin.getName() + " is outdated.");
                if(core.autoUpdate) {
                    Logging.Log("Attempting auto-update.");
                    update(null);
                }
                break;

            case 0:
                Logging.Log("Hooked plugin: " + plugin.getName() + " is up to date.");
                break;

            case 1:
                Logging.Log("Hooked plugin: " + plugin.getName() + " is running a pre-release/snapshot/development build.");
                break;

            case -2:
                Logging.Err("Hooked plugin: " + plugin.getName() + " is incorrectly configured (Malformed update URL).");

            default:
                plugin.errors.add(new Exception("Spigot Updater error"));
                break;
        }
    }

    @Override
    String getLatestVersion() {
        try {
            JSONObject json = readJsonFromUrl("https://api.spiget.org/v2/resources/" + PluginID + "/versions/latest");
            return json.getString("name");
        } catch (Exception e) {
            Logging.Err(plugin, e.toString());
            plugin.errors.add(e);
            return null;
        }
    }

    @Override
    protected int update(@Nullable URL url) {
        try {
            return super.update(new URL("https://cdn.spiget.org/file/spiget-resources/" + PluginID + ".jar"));
        } catch (MalformedURLException e) {
            return -2;
        }
    }
}
