package uk.co.benkeoghcgd.api.AxiusCore.Utils;

import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlugin;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

public final class Logging {
    static Logger log = Logger.getLogger("Minecraft");
    static HashMap<UUID, String> errorLog = new HashMap<>();

    public static void Log(AxiusPlugin sender, String msg) {
        sender.lastUpdate = System.currentTimeMillis();
        log.info("[" + sender.getName() + "] " + msg);
    }

    public static void Err(AxiusPlugin sender, String msg) {
        sender.lastUpdate = System.currentTimeMillis();
        log.severe("[" + sender.getName() + "] " + msg);
        errorLog.put(UUID.randomUUID(), "&c&lERROR&8 - &7" + msg);
    }

    public static void Warn(AxiusPlugin sender, String msg) {
        sender.lastUpdate = System.currentTimeMillis();
        log.warning("[" + sender.getName() + "] " + msg);
    }

    public static void Log(String msg) {
        log.info("[CORE] " + msg);
    }

    public static void Err(String msg) {
        log.severe("[CORE] " + msg);
        errorLog.put(UUID.randomUUID(), "&c&lERROR&8 - &7" + msg);
    }

    public static void Warn(String msg) {
        log.warning("[CORE] " + msg);
    }
}
