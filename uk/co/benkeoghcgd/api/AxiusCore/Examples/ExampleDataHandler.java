package uk.co.benkeoghcgd.api.AxiusCore.Examples;

import org.bukkit.plugin.java.JavaPlugin;
import uk.co.benkeoghcgd.api.AxiusCore.API.DataHandler;

public class ExampleDataHandler extends DataHandler {
    public ExampleDataHandler(JavaPlugin instance) {
        super(instance, "Example");
    }

    @Override
    protected void saveDefaults() {

    }
}
