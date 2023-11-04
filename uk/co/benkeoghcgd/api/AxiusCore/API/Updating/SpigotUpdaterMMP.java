package uk.co.benkeoghcgd.api.AxiusCore.API.Updating;

import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlugin;

public class SpigotUpdaterMMP extends SpigotUpdater {
    public SpigotUpdaterMMP(AxiusPlugin plugin, int PluginID) {
        super(plugin, PluginID);
    }

    @Override
    protected int compare() {
        return super.compareMMP();
    }
}
