package uk.co.benkeoghcgd.api.AxiusCore.API.Utilities;

import net.lenni0451.spm.utils.PluginUtils;
import net.lenni0451.spm.utils.ReflectionUtils;
import net.lenni0451.spm.utils.ThreadUtils;
import org.bukkit.Bukkit;
import org.json.JSONException;
import org.json.JSONObject;
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlugin;
import uk.co.benkeoghcgd.api.AxiusCore.API.Enums.VersionFormat;
import uk.co.benkeoghcgd.api.AxiusCore.AxiusCore;
import uk.co.benkeoghcgd.api.AxiusCore.Exceptions.CoreSelfUpdateException;
import uk.co.benkeoghcgd.api.AxiusCore.Utils.Logging;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class Updater {

    public static AxiusCore core = AxiusCore.getInstance();
    private final AxiusPlugin plugin;
    private final int PluginID;
    private final PluginUtils pluginUtils;


    public Updater(AxiusPlugin plugin, int PluginID) throws CoreSelfUpdateException {
        this.plugin = plugin;
        this.PluginID = PluginID;
        this.pluginUtils = new PluginUtils();

        String latestVersion = getVersionString();
        if(!plugin.getDescription().getVersion().equals(latestVersion)) {
            Logging.Log(plugin, "Hooked plugin: " + plugin.getName() + ", is out of date!. Latest version: " + latestVersion + ", Current version: " + plugin.getDescription().getVersion());
            if(plugin != core) {
                Logging.Log(plugin, "Attempting Auto-Update.. (Spigot Resource ID: " + PluginID + ")");
                int output = SelfInstall();
                if(output != 1) {
                    throw new CoreSelfUpdateException("Error while updating.", null);
                }
                Logging.Log(plugin, "Auto-Update Successful.");
            }
            else {
                Bukkit.getScheduler().runTaskLater(plugin, ()-> {
                    Logging.Log("Attempting Auto-Update.. (Spigot Resource ID: " + PluginID + ")");
                    int output = SelfInstall();
                    if(output != 1) {
                        Logging.Err("Error while self-updating.");
                    }
                    Logging.Log("Auto-Update Successful.");
                }, 1);
            }
        }
        else {
            Logging.Log("Hooked plugin: " + plugin.getName() + ", is up to date.");
        }
    }
    public Updater(AxiusPlugin plugin, int PluginID, VersionFormat format, String versionSeperator) throws CoreSelfUpdateException {
        this.plugin = plugin;
        this.PluginID = PluginID;
        this.pluginUtils = new PluginUtils();

        int major = -1, minor = -1, patch = -1;
        int omajor = -1, ominor = -1, opatch = -1;

        String latestVersion = getVersionString();
        String[] latestParts = latestVersion.split(versionSeperator);
        String[] oParts = plugin.getDescription().getVersion().split(versionSeperator);

        switch (latestParts.length) {
            case 3:
                patch = Integer.parseInt(latestParts[2].replaceAll("\\D", ""));
            case 2:
                major = Integer.parseInt(latestParts[0].replaceAll("\\D", ""));
                minor = Integer.parseInt(latestParts[1].replaceAll("\\D", ""));
                break;
        }

        switch (oParts.length) {
            case 3:
                opatch = Integer.parseInt(oParts[2].replaceAll("\\D", ""));
            case 2:
                omajor = Integer.parseInt(oParts[0].replaceAll("\\D", ""));
                ominor = Integer.parseInt(oParts[1].replaceAll("\\D", ""));
                break;
        }

        boolean outdated = false;
        boolean inDev = false;

        if(major > omajor) outdated = true;
        else if(major < omajor) inDev = true;
        else {
            if(minor > ominor) outdated = true;
            else if(minor < ominor) inDev = true;
            else if(patch != -1 && opatch != -1) {
                if(patch > opatch) outdated = true;
                else if (patch < opatch) inDev = true;
            }
        }

        if(outdated) {
            Logging.Log("Hooked plugin: " + plugin.getName() + ", is out of date!. Latest version: " + latestVersion + ", Current version: " + plugin.getDescription().getVersion());
            if(plugin != core) {
                Logging.Log("Attempting Auto-Update.. (Spigot Resource ID: " + PluginID + ")");
                int output = SelfInstall();
                if(output != 1) {
                    throw new CoreSelfUpdateException("Error while updating.", null);
                }
                Logging.Log("Auto-Update Successful.");
            }
            else {
                Bukkit.getScheduler().runTaskLater(plugin, ()-> {
                    Logging.Log("Attempting Auto-Update.. (Spigot Resource ID: " + PluginID + ")");
                    int output = SelfInstall();
                    if(output != 1) {
                        Logging.Err("Error while self-updating.");
                        return;
                    }
                    else if(output == -2) {
                        Logging.Log("Skipping auto-update, as it is disabled in the configuration.");
                    }
                    Logging.Log("Auto-Update Successful.");
                }, 1);
            }
        }
        else if (inDev) {
            Logging.Log("Hooked plugin: " + plugin.getName() + ", is running an in-development build, and therefore may be unstable.");
        }
        else {
            Logging.Log("Hooked plugin: " + plugin.getName() + ", is up to date.");
        }
    }

    /**
     * Retrieve the version string from SpiGET API
     *
     * @return version string
     */
    public String getVersionString() {
        try {
            JSONObject json = readJsonFromUrl("https://api.spiget.org/v2/resources/" + PluginID + "/versions/latest");
            return json.getString("name");
        } catch (Exception e) {
            Logging.Err(plugin, e.toString());
            plugin.errors.add(e);
            return null;
        }
    }

    /**
     * Call the updater to automatically update the plugin it is being called from
     *
     * @return integer depending on install result
     */
    public int SelfInstall() {
        if(!core.autoUpdate) return -2;

        AtomicInteger output = new AtomicInteger(-1);
        try {
            {
                Class.forName(ThreadUtils.class.getName());
                Class.forName(ReflectionUtils.class.getName());
                Class.forName(FileOutputStream.class.getName());
            }

            try {
                this.pluginUtils.unloadPlugin(plugin);
                {
                    try (InputStream in = new URL("https://cdn.spiget.org/file/spiget-resources/" + PluginID + ".jar").openStream()) {
                        Files.copy(in, Paths.get(plugin.pluginFile().getAbsolutePath()), REPLACE_EXISTING);
                        output.set(1);
                        PublicPluginData ppd = plugin.GetPublicPluginData();
                        ppd.setRegisterStatus(false);
                        plugin.SetPublicPluginData(ppd);
                    }
                    catch (Exception e) {
                        String reason = "Unknown Exception";
                        if(e instanceof MalformedURLException) reason = "Invalid URL";
                        if(e instanceof IOException) reason = "Error during file transfer";

                        Logging.Err(plugin, reason);
                        throw new CoreSelfUpdateException(reason, e);
                    }
                }
                if(plugin != core) this.pluginUtils.loadPlugin(plugin);
                else Bukkit.reload();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return output.get();
    }

    /**
     * Reader to String
     *
     * @param rd Reader to parse
     * @return String parsed from Reader
     *
     * @throws IOException Read from builder failed
     */
    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    /**
     * Retrieve a JSONObject from URL
     *
     * @param url URL String to grab JSONObject from
     * @return JSONObject from URL data
     *
     * @throws IOException      Invalid URL
     * @throws JSONException    JSON cannot be parsed from given URL
     */
    private JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        }
    }
}
