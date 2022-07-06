package uk.co.benkeoghcgd.api.AxiusCore.API;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import uk.co.benkeoghcgd.api.AxiusCore.Utils.Logging;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

public abstract class DataHandler {
    public HashMap<String, Object> data = new HashMap<>();
    public File file;
    public AxiusPlugin plugin;

    /**
     * Function should be overriden and paired with setData to set default configuration data
     */
    protected abstract void saveDefaults();

    /**
     * Constructor for data handlers, specifically in the yml format.
     * providing these parameters generates a data file, if one doesn't exist, or opens the existing
     * file if the given data file does exist.
     * @param instance your instance
     * @param filename your filename
     */
    public DataHandler(AxiusPlugin instance, String filename) {
        file = new File(instance.getDataFolder() + File.separator + filename + ".yml");
        plugin = instance;
        init();
    }

    private void init() {
        if(!file.exists()) {
            saveDefaults();
            try {
                if(!file.createNewFile()) {
                    Logging.Err(plugin, "File already exists!");
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        loadData();
    }

    /**
     * Set a key-value pair into the data file.
     * For example: yourDataInstance#setData("messages.join", "[+] %s joined the game");
     *            : yourDataInstance#setData("Data.list", new ArrayList<String>());
     * @param Key   key of pair, with leading path
     * @param value data of pair, stored at key
     */
    public void setData(String Key, Object value) {
        setData(Key, value, true);
    }


    /**
     * Set a key-value pair into the data file.
     * For example: yourDataInstance#setData("messages.join", "[+] %s joined the game", false); -> once set, won't change
     *            : yourDataInstance#setData("Data.list", new ArrayList<String>(), true); -> always changes to an empty list
     * @param key   key of pair, with leading path
     * @param value data of pair, stored at key
     * @param override should the function override the data already present
     */
    public void setData(String key, Object value, boolean override) {
        FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
        if(!fileConfig.isSet(key) || override) {
            fileConfig.set(key, value);
            try {
                fileConfig.save(file);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * load all data from the given data file and place within the data map.
     */
    public void loadData() {
        FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
        for(String key : Objects.requireNonNull(fileConfig.getConfigurationSection("")).getKeys(true)) {
            this.data.put(key, fileConfig.get(key));
        }

    }
}