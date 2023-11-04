package uk.co.benkeoghcgd.api.AxiusCore.API.Exceptions;

import org.bukkit.command.Command;

public class CommandRegisterException extends Exception {
    public CommandRegisterException(Command cmd) {
        super("Command Register Exception: " + cmd.getName());
    }
}
