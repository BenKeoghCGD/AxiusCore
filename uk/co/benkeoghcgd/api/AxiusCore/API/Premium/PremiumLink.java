package uk.co.benkeoghcgd.api.AxiusCore.API.Premium;

import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlugin;
import uk.co.benkeoghcgd.api.AxiusCore.Exceptions.InvalidPremiumAuthException;
import uk.co.benkeoghcgd.api.AxiusCore.Utils.Logging;

import java.net.HttpURLConnection;
import java.net.URL;

public class PremiumLink {
    public PremiumLink(AxiusPlugin instance, Integer pluginId) throws InvalidPremiumAuthException
    {
        try {
            URL url = new URL("http://api.benkeoghcgd.co.uk/licensing/checkRegistry.php?pid=" + pluginId);
            HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
            switch(urlc.getResponseCode()) {

            }
            urlc.disconnect();
        } catch (Exception e) {
            throw new InvalidPremiumAuthException("Unable to reach licensing server.");
        }
    }
}
