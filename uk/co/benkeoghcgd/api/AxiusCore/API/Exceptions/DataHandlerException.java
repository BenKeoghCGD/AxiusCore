package uk.co.benkeoghcgd.api.AxiusCore.API.Exceptions;

public class DataHandlerException extends Exception {
    public DataHandlerException(String fileName, String error) {
        super("Data Handling Error (FILE: " + fileName + "): " + error);
    }
}
