package uk.co.benkeoghcgd.api.AxiusCore.API.Utilities;

import net.lenni0451.spm.utils.PluginUtils;
import net.lenni0451.spm.utils.ReflectionUtils;
import net.lenni0451.spm.utils.ThreadUtils;
import org.bukkit.Bukkit;
import org.checkerframework.checker.units.qual.C;
import org.json.JSONException;
import org.json.JSONObject;
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlugin;
import uk.co.benkeoghcgd.api.AxiusCore.AxiusCore;
import uk.co.benkeoghcgd.api.AxiusCore.Exceptions.CoreSelfUpdateException;
import uk.co.benkeoghcgd.api.AxiusCore.Utils.Logging;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class Updater {

    private AxiusCore core;
    private AxiusPlugin plugin;
    private int PluginID;

    private PluginUtils pluginUtils;

    public Updater(AxiusPlugin plugin, int PluginID) throws CoreSelfUpdateException {
        this.plugin = plugin;
        this.PluginID = PluginID;
        this.core = plugin.core;
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
                    Logging.Log(plugin, "Attempting Auto-Update.. (Spigot Resource ID: " + PluginID + ")");
                    int output = SelfInstall();
                    if(output != 1) {
                        Logging.Err("Error while self-updating.");
                    }
                    Logging.Log(plugin, "Auto-Update Successful.");
                }, 1);
            }
        }
        else {
            Logging.Log(plugin, "Hooked plugin: " + plugin.getName() + ", is up to date.");
        }
    }

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

    public int SelfInstall() {
        AtomicInteger output = new AtomicInteger(-1);
        try {
            { //Load all classes needed for the PluginUtils here because as soon as we overwrite the plugin jar we are no longer able to load classes
                Class.forName(ThreadUtils.class.getName());
                Class.forName(ReflectionUtils.class.getName());
                Class.forName(FileOutputStream.class.getName());
            }

            try {
                this.pluginUtils.unloadPlugin(plugin);
                //Here the ClassLoader is already closed. There is no going back now
                {
                    try (InputStream in = new URL("https://cdn.spiget.org/file/spiget-resources/" + PluginID + ".jar").openStream()) {
                        Files.copy(in, Paths.get(plugin.pluginFile().getAbsolutePath()), REPLACE_EXISTING);
                        output.set(1);
                        PublicPluginData ppd = plugin.GetPublicPluginData();
                        ppd.canRegister = false;
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


    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }
}
