package uk.co.benkeoghcgd.api.AxiusCore.API.Exceptions;

public class MissingDependException extends Exception {
    public MissingDependException(String plugin) {
        super("Missing Dependency: " + plugin);
    }
}
