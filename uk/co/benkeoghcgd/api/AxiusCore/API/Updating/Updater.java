package uk.co.benkeoghcgd.api.AxiusCore.API.Updating;

import net.lenni0451.spm.utils.PluginUtils;
import net.lenni0451.spm.utils.ReflectionUtils;
import net.lenni0451.spm.utils.ThreadUtils;
import org.bukkit.Bukkit;
import org.json.JSONException;
import org.json.JSONObject;
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlugin;
import uk.co.benkeoghcgd.api.AxiusCore.API.PluginData.PublicPluginData;
import uk.co.benkeoghcgd.api.AxiusCore.AxiusCore;
import uk.co.benkeoghcgd.api.AxiusCore.API.Exceptions.CoreSelfUpdateException;
import uk.co.benkeoghcgd.api.AxiusCore.Utils.Logging;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public abstract class Updater {
    protected AxiusPlugin plugin;
    protected AxiusCore core = AxiusCore.getInstance();
    protected final PluginUtils pluginUtils = new PluginUtils();

    public Updater(AxiusPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Retrieve the latest version
     * @return version string
     */
    abstract String getLatestVersion();

    /**
     * Retrieve the current version
     * @return version string
     */
    protected String getCurrentVersion() {
        return plugin.getDescription().getVersion();
    }

    /**
     * Compare the latest version to the current version
     * RECOMMENDED: override this method and provide your own way to compare versions
     *
     * @return 0 if both versions are the same, -1 if the latest version is newer
     */
    protected int compare() {
        if(getCurrentVersion().equalsIgnoreCase(getLatestVersion())) return 0;
        else return -1;
    }

    /**
     * Update and replace current file from URL
     * @return output based on result.
     */
    protected int update(URL url) {
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
                    try (InputStream in = url.openStream()) {
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

    // JSON UTILITIES

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
    protected JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        }
    }

    protected int compareMMP() {
        String latest = getLatestVersion(), current = getCurrentVersion();
        assert latest != null;
        assert current != null;

        String[] latestParts = latest.split("\\.");
        String[] currentParts = current.split("\\.");

        int length = Math.max(currentParts.length, latestParts.length);

        for(int i = 0; i < length; i++) {
            int currentPart = i < currentParts.length ?
                    Integer.parseInt(currentParts[i]) : 0;
            int latestPart = i < latestParts.length ?
                    Integer.parseInt(latestParts[i]) : 0;

            if(currentPart < latestPart) return -1; // outdated
            if(currentPart > latestPart) return 1; // pre-release
        }

        return 0;
    }
}
