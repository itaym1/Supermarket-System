package serviceObjects;

public class Response {
    private String ErrorMessage;
    private boolean ErrorOccurred;

    public Response(){ErrorOccurred = false;}

    public Response(String errorMessage) {
        ErrorMessage = errorMessage;
        ErrorOccurred = errorMessage != null;
    }

    public boolean isErrorOccured() {
        return ErrorOccurred;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }
}