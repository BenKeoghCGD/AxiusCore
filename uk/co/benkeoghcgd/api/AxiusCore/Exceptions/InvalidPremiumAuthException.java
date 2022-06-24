package uk.co.benkeoghcgd.api.AxiusCore.Exceptions;

public class InvalidPremiumAuthException extends Exception {
    public InvalidPremiumAuthException(String msg) {
        super("Invalid Premium Authentication: " + msg);
    }
}
