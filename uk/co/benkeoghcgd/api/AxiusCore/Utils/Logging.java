package uk.co.benkeoghcgd.api.AxiusCore.Utils;

import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlugin;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

public final class Logging {
    static Logger log = Logger.getLogger("Minecraft");
    static HashMap<UUID, String> errorLog = new HashMap<>();

    /**
     * Use the core logging system to send a Log message type to the console.
     * This function uses the global format of AxiusPlugin logging.
     *
     * @param sender Instance of an AxiusPlugin which is calling the log
     * @param msg    Message to send to console
     */
    public static void Log(AxiusPlugin sender, String msg) {
        log.info("[" + sender.getName() + "] " + msg);
    }

    /**
     * Use the core logging system to send a Error message type to the console.
     * This function uses the global format of AxiusPlugin logging.
     *
     * @param sender Instance of an AxiusPlugin which is calling the error
     * @param msg    Message to send to console
     */
    public static void Err(AxiusPlugin sender, String msg) {
        log.severe("[" + sender.getName() + "] " + msg);
        errorLog.put(UUID.randomUUID(), "&c&lERROR&8 - &7" + msg);
    }

    /**
     * Use the core logging system to send a Warning message type to the console.
     * This function uses the global format of AxiusPlugin logging.
     *
     * @param sender Instance of an AxiusPlugin which is calling the warning
     * @param msg    Message to send to console
     */
    public static void Warn(AxiusPlugin sender, String msg) {
        log.warning("[" + sender.getName() + "] " + msg);
    }


    /**
     * Use the core logging system to send a Log message type to the console.
     * This function uses the global format of AxiusPlugin logging, and sends the message on behalf of the core itself.
     *
     * @param msg    Message to send to console
     */
    public static void Log(String msg) {
        log.info("[CORE] " + msg);
    }

    /**
     * Use the core logging system to send a Error message type to the console.
     * This function uses the global format of AxiusPlugin logging, and sends the message on behalf of the core itself.
     *
     * @param msg    Message to send to console
     */
    public static void Err(String msg) {
        log.severe("[CORE] " + msg);
        errorLog.put(UUID.randomUUID(), "&c&lERROR&8 - &7" + msg);
    }

    /**
     * Use the core logging system to send a Warning message type to the console.
     * This function uses the global format of AxiusPlugin logging, and sends the message on behalf of the core itself.
     *
     * @param msg    Message to send to console
     */
    public static void Warn(String msg) {
        log.warning("[CORE] " + msg);
    }
}
