package uk.co.benkeoghcgd.api.AxiusCore.Exceptions;

import org.bukkit.command.Command;

public class CommandRegisterException extends Exception {
    public CommandRegisterException(Command cmd) {
        super("Command Register Exception: " + cmd.getName());
    }
}
