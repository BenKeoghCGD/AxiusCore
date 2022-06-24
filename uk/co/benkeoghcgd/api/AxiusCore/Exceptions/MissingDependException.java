package uk.co.benkeoghcgd.api.AxiusCore.Exceptions;

public class MissingDependException extends Exception {
    public MissingDependException(String plugin) {
        super("Missing Dependency: " + plugin);
    }
}
