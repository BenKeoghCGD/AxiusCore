package uk.co.benkeoghcgd.api.AxiusCore.DataHandlers;

import uk.co.benkeoghcgd.api.AxiusCore.API.Utilities.DataHandler;
import uk.co.benkeoghcgd.api.AxiusCore.AxiusCore;

public class ConfigYML extends DataHandler {
    AxiusCore core;
    public ConfigYML(AxiusCore axiusCore) {
        super(axiusCore, "Config");
        this.core = axiusCore;
    }

    @Override
    protected void saveDefaults() {
        setData("autoUpdatePlugins", false);
    }
}
