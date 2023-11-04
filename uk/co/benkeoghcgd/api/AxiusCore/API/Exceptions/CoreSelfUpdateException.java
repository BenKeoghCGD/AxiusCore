package uk.co.benkeoghcgd.api.AxiusCore.API.Exceptions;

public class CoreSelfUpdateException extends Exception {
    public CoreSelfUpdateException(String reason, Exception fullException) {
        super(reason);
        addSuppressed(fullException);
    }
}
