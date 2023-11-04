package uk.co.benkeoghcgd.api.AxiusCore.DataHandlers;

import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlayer;
import uk.co.benkeoghcgd.api.AxiusCore.API.Utilities.DataHandler;
import uk.co.benkeoghcgd.api.AxiusCore.AxiusCore;

public class PlayerYML extends DataHandler {

    public PlayerYML(AxiusPlayer player) {
        super(AxiusCore.getInstance(), "Data/" + player.getUUIDString() );
    }

    @Override
    protected void saveDefaults() {
        setData("Streamer", false);
    }
}
