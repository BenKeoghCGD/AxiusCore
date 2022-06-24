package uk.co.benkeoghcgd.api.AxiusCore.Exceptions;

public class CoreFunctionalityFailure extends Exception {
    public CoreFunctionalityFailure(String msg) {
        super("Functionality of the core has failed: " + msg);
    }
}
