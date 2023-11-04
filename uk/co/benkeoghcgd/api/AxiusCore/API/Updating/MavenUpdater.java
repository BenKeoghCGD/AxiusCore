package uk.co.benkeoghcgd.api.AxiusCore.API.Updating;

import org.w3c.dom.*;
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlugin;
import uk.co.benkeoghcgd.api.AxiusCore.Utils.Logging;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.MalformedURLException;
import java.net.URL;

public class MavenUpdater extends Updater{

    String repository = null;

    public MavenUpdater(AxiusPlugin plugin, String repositoryURL) {
        super(plugin);
        repository = repositoryURL;

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
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(repository + "/maven-metadata.xml");
            Logging.Log(doc.getElementsByTagName("release").item(0).getNodeValue());
            return doc.getElementsByTagName("release").item(0).getNodeValue();
        } catch (Exception e) {
            Logging.Err(plugin, e.toString());
            plugin.errors.add(e);
            return null;
        }
    }

    @Override
    protected int update(URL url) {
        try {
            return super.update(new URL(repository + "/" + plugin.getName() + "-" + getLatestVersion() + ".jar"));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
